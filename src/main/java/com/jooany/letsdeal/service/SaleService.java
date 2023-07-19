package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.controller.dto.request.SaleCreateReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.CategoryRepository;
import com.jooany.letsdeal.repository.ImageRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import com.jooany.letsdeal.repository.UserRepository;
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
    private final AwsS3Service awsS3Service;

    @Transactional
    public void createSale(SaleCreateReq req, @Nullable List<MultipartFile> imageFiles, String userName) throws IOException {
        User user = getUserOrException(userName);
        Category category = getCategoryOrException(req.getCategoryId());

        //Sale 생성
        Sale sale = Sale.of(user, category, req.getTitle(), req.getContents(), req.getSellerPrice());

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

        saleRepository.save(sale);
    }

    private User getUserOrException(String userName){
        return userRepository.findByUserName(userName).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 사용자를 찾을 수 없습니다.", userName)));
    }

    private Category getCategoryOrException(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public Page<SaleListRes> getSaleList(SearchCondition condition, Pageable pageable, String userName) {
        // 현재 로그인한 사용자 검색 조건에 저장
        condition.setCurrentUserName(userName);

        return saleRepository.findAllBySearchCondition(condition, pageable);
    }

    public SaleRes getSaleInfo(Long saleId){
        SaleRes saleInfo = getSaleOrException(saleId);

        List<ImageDto> images = getImageOrException(saleId);
        saleInfo.setImages(images);

        return saleInfo;
    }

    private SaleRes getSaleOrException(Long saleId){
        return saleRepository.findSaleResById(saleId).orElseThrow(() ->
                new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
    }

    private List<ImageDto> getImageOrException(Long saleId){
        List<ImageDto> images = imageRepository.findAllBySaleIdAndOrderBySortOrderAsc(saleId)
                .stream().map(ImageDto::from).collect(Collectors.toList());

        if(ObjectUtils.isEmpty(images)){
            throw new LetsDealAppException(ErrorCode.IMAGE_NOT_FOUND);
        }

        return images;
    }

}
