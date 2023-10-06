package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.*;
import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.jooany.letsdeal.model.enumeration.UserRole;
import com.jooany.letsdeal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService {
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProposalRepository proposalRepository;
    private final AwsS3Service awsS3Service;

    @Transactional(readOnly = true)
    public Page<SaleRes> getSaleList(SearchCondition condition, Pageable pageable, String userName) {
        condition.setCurrentUserName(userName);

        return saleRepository.findAllBySearchCondition(condition, pageable);
    }

    @Transactional(readOnly = true)
    public SaleInfoRes getSaleInfo(Long saleId){
        SaleInfoRes saleInfo = getSaleInfoResOrException(saleId);

        List<ImageDto> images = getImageOrException(saleId);
        saleInfo.setImages(images);
        return saleInfo;
    }

    @Transactional
    public void saveSale(SaleSaveReq req, @Nullable List<MultipartFile> imageFiles, String userName) throws IOException {
        User user = getUserOrException(userName);
        Category category = getCategoryOrException(req.getCategoryId());

        //Sale 생성
        Sale sale = Sale.of(user, category, req.getTitle(), req.getContents(), req.getSellerPrice());

        // 이미지 s3 업로드 및 url 반환
        uploadImages(imageFiles, sale);

        saleRepository.save(sale);
    }
    @Transactional
    public void updateSale(Long id, Long currentUserId, UserRole currentUserRole, SaleSaveReq req, @Nullable List<MultipartFile> imageFiles) throws IOException {
//        User currentUser = getUserOrException(userName);
        Sale sale = getSaleOrException(id);
        Category category = getCategoryOrException(req.getCategoryId());

        // 로그인 사용자가 판매글 작성자도 아니고 관리자도 아닐 때 에러 발생
        if(sale.getUser().getId() != currentUserId && currentUserRole != UserRole.ADMIN){
            throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
        }

        sale.update(category, req.getTitle(), req.getContents(), req.getSellerPrice());

        // 이미지 삭제 처리
        List<Image> images = sale.getImages();
        for (Image image : images) {
            awsS3Service.deleteImage(image.getImageUrl());
            imageRepository.delete(image);
        }
        sale.getImages().clear();

        // 이미지 s3 업로드 및 url 반환
        uploadImages(imageFiles, sale);
    }

    @Transactional
    public void deleteSale(Long id, String userName){
        User currentUser = getUserOrException(userName);
        Sale sale = getSaleOrException(id);

        // 로그인 사용자가 판매글 작성자도 아니고 관리자도 아닐 때 에러 발생
        if(sale.getUser() != currentUser && currentUser.getUserRole() != UserRole.ADMIN){
            throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
        }

        // s3에 저장된 이미지 삭제
        List<Image> images = sale.getImages();
        for (Image image : images) {
            awsS3Service.deleteImage(image.getImageUrl());
        }
        // db에 저장된 sale의 이미지, 가격제안 데이터 삭제
        imageRepository.deleteAllBySale(sale);
        proposalRepository.deleteAllBySale(sale);

        // sale은 실제로 삭제하지 않고, deleted_at을 업데이트
        saleRepository.softDeleteById(id);
    }

    @Transactional(readOnly = true)
    public ProposalListRes getProposalList(Long saleId, Pageable pageable, String userName){
        checkIsSaleExist(saleId);
        Page<ProposalRes> proposalList = proposalRepository.findAllBySaleId(saleId, userName, pageable);
        List<MyProposalRes> myProposalList = proposalRepository.findAllBySaleIdAndUserName(saleId, userName);

        ProposalListRes proposalListRes = new ProposalListRes(proposalList, myProposalList);
        return proposalListRes;
    }

    @Transactional
    public void saveProposal(Long saleId, Integer buyerPrice, String userName){
        User user = getUserOrException(userName);
        Sale sale = getSaleOrException(saleId);
        Proposal proposal = Proposal.of(user, sale, buyerPrice);
        proposalRepository.save(proposal);
    }

    @Transactional
    public void deleteProposal(Long saleId, Long proposalId, String userName){
        User user = getUserOrException(userName);
        checkIsSaleExist(saleId);
        Proposal proposal = getProposalOrException(proposalId);
        if(proposal.getUser() != user){
            throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
        }

        proposalRepository.delete(proposal);
    }

    @Transactional
    public void refuseProposal(Long saleId, Long proposalId, String userName){
        User user = getUserOrException(userName);
        Sale sale = getSaleOrException(saleId);
        if(sale.getUser() != user){
            throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
        }
        Proposal proposal = getProposalOrException(proposalId);
        proposal.setProposalStatus(ProposalStatus.REFUSED);
        proposalRepository.save(proposal);
    }

    private User getUserOrException(String userName){
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 사용자를 찾을 수 없습니다.", userName)));
    }

    private Category getCategoryOrException(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private Sale getSaleOrException(Long saleId){
        return saleRepository.findById(saleId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
    }

    private SaleInfoRes getSaleInfoResOrException(Long saleId){
        return saleRepository.findSaleInfoResById(saleId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
    }

    private void checkIsSaleExist(Long saleId){
        if(saleRepository.countSaleById(saleId) == 0){
            throw new LetsDealAppException(ErrorCode.SALE_NOT_FOUND);
        }
    }

    private Proposal getProposalOrException(Long proposalId){
        return proposalRepository.findById(proposalId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.PROPOSAL_NOT_FOUND));
    }

    private List<ImageDto> getImageOrException(Long saleId){
        List<ImageDto> images = imageRepository.findAllBySaleIdAndOrderBySortOrderAsc(saleId)
                .stream().map(ImageDto::from).collect(Collectors.toList());

        if(ObjectUtils.isEmpty(images)){
            throw new LetsDealAppException(ErrorCode.IMAGE_NOT_FOUND);
        }

        return images;
    }

    private void uploadImages(List<MultipartFile> imageFiles, Sale sale) throws IOException {
        // 이미지 s3 업로드 및 url 반환
        if(imageFiles != null && !imageFiles.isEmpty()) {
            int order = 1;

            for(MultipartFile file : imageFiles) {
                String imageUrl = awsS3Service.saveImageToS3(file);
                sale.addImage(Image.of(sale, imageUrl, order++));
            }
        }else {
            throw new LetsDealAppException(ErrorCode.EMPTY_IMAGE);
        }
    }

}
