package com.jooany.letsdeal.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.jooany.letsdeal.model.enumeration.UserRole;
import com.jooany.letsdeal.repository.CategoryRepository;
import com.jooany.letsdeal.repository.ImageRepository;
import com.jooany.letsdeal.repository.ProposalRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import com.jooany.letsdeal.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaleService {
	private final SaleRepository saleRepository;
	private final UserRepository userRepository;
	private final CategoryRepository categoryRepository;
	private final ImageRepository imageRepository;
	private final ProposalRepository proposalRepository;
	// private final AwsS3Service awsS3Service;
	private final RedissonClient redissonClient;

	@Transactional(readOnly = true)
	public Page<SaleRes> getSaleList(SearchCondition condition, Pageable pageable, String userName) {
		condition.setCurrentUserName(userName);

		return saleRepository.findAllBySearchCondition(condition, pageable);
	}

	@Transactional(readOnly = true)
	public SaleInfoRes getSaleInfo(Long saleId) {
		SaleInfoRes saleInfo = getSaleInfoResOrException(saleId);

		List<ImageDto> images = getImageOrException(saleId);
		saleInfo.setImages(images);
		return saleInfo;
	}

	@Transactional
	public void saveSale(SaleSaveReq req, @Nullable List<MultipartFile> imageFiles, String userName)
		throws IOException {
		User user = getUserOrException(userName);
		Category category = getCategoryOrException(req.getCategoryId());

		Sale sale = Sale.builder(user, category, req.getTitle(), req.getContents(), req.getSellerPrice()).build();

		uploadImages(imageFiles, sale);

		saleRepository.save(sale);
	}

	@Transactional
	public void updateSale(Long id, Long currentUserId, UserRole currentUserRole, SaleSaveReq req,
		@Nullable List<MultipartFile> imageFiles) throws IOException {
		Sale sale = getSaleOrException(id);
		Category category = getCategoryOrException(req.getCategoryId());

		if (sale.getWriter().getId() != currentUserId && currentUserRole != UserRole.ADMIN) {
			throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
		}

		sale.update(category, req.getTitle(), req.getContents(), req.getSellerPrice());

		List<Image> images = sale.getImages();
		for (Image image : images) {
			// awsS3Service.deleteImage(image.getImageUrl());
			imageRepository.delete(image);
		}
		sale.getImages().clear();

		uploadImages(imageFiles, sale);
	}

	@Transactional
	public void deleteSale(Long id, String userName) {
		User currentUser = getUserOrException(userName);
		Sale sale = getSaleOrException(id);

		if (sale.getWriter() != currentUser && currentUser.getUserRole() != UserRole.ADMIN) {
			throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
		}

		// for (Image image : sale.getImages()) {
		// awsS3Service.deleteImage(image.getImageUrl());
		// }

		imageRepository.deleteAllBySale(sale);
		proposalRepository.deleteAllBySale(sale);
		saleRepository.softDeleteById(id);
	}

	@Transactional(readOnly = true)
	public ProposalListRes getProposalList(Long saleId, Pageable pageable, String userName) {
		checkIsSaleExist(saleId);
		Page<ProposalRes> proposalList = proposalRepository.findAllBySaleId(saleId, userName, pageable);
		List<MyProposalRes> myProposalList = proposalRepository.findAllBySaleIdAndUserName(saleId, userName);

		ProposalListRes proposalListRes = new ProposalListRes(proposalList, myProposalList);
		return proposalListRes;
	}

	@Transactional
	public void saveProposal(Long saleId, Integer buyerPrice, String userName) {
		User buyer = getUserOrException(userName);
		Sale sale = getSaleOrException(saleId);
		Proposal proposal = Proposal.builder(buyer, sale, buyerPrice).build();
		checkMaxPriceProposalAndSaveToSale(sale, proposal);
		proposalRepository.save(proposal);
	}

	private void checkMaxPriceProposalAndSaveToSale(Sale sale, Proposal proposal) {
		Proposal maxPriceProposal = sale.getMaxPriceProposal();
		Integer maxBuyerPrice = 0;

		if (maxPriceProposal != null) {
			maxBuyerPrice = maxPriceProposal.getBuyerPrice();
		}

		if (maxBuyerPrice == null || maxBuyerPrice < proposal.getBuyerPrice()) {
			sale.updateMaxPriceProposal(proposal);
		}
	}

	@Transactional
	public void deleteProposal(Long saleId, Long proposalId, String userName) {
		User user = getUserOrException(userName);
		Proposal proposal = getProposalOrException(proposalId);

		if (proposal.getBuyer() != user) {
			throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
		}

		Sale sale = getSaleOrException(saleId);
		updateMaxPriceProposal(sale, proposal);
		proposalRepository.delete(proposal);
	}

	private void updateMaxPriceProposal(Sale sale, Proposal proposal) {

		if (sale.getMaxPriceProposal() != null &&
			sale.getMaxPriceProposal().getId().equals(proposal.getId())) {

			List<Proposal> proposals = sale.getProposals();
			proposals.remove(proposal);

			if (proposals.isEmpty()) {
				sale.updateMaxPriceProposal(null);

			} else {
				Proposal maxPriceProposal = null;
				Integer maxBuyerPrice = 0;

				for (Proposal p : proposals) {
					Integer buyerPrice = p.getBuyerPrice();

					if (maxBuyerPrice < buyerPrice) {
						maxBuyerPrice = buyerPrice;
						maxPriceProposal = p;
					}
				}

				sale.updateMaxPriceProposal(maxPriceProposal);
			}
		}
	}

	@Transactional
	public void refuseProposal(Long saleId, Long proposalId, String userName) {
		User user = getUserOrException(userName);
		Sale sale = getSaleOrException(saleId);
		if (sale.getWriter() != user) {
			throw new LetsDealAppException(ErrorCode.INVALID_PERMISSION);
		}
		Proposal proposal = getProposalOrException(proposalId);
		proposal.updateProposalStatus(ProposalStatus.REFUSED);
		proposalRepository.save(proposal);
	}

	private User getUserOrException(String userName) {
		return userRepository.findByUserName(userName).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 사용자를 찾을 수 없습니다.", userName)));
	}

	private Category getCategoryOrException(Long categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	private Sale getSaleOrException(Long saleId) {
		return saleRepository.findById(saleId).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
	}

	private SaleInfoRes getSaleInfoResOrException(Long saleId) {
		return saleRepository.findSaleInfoResById(saleId).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.SALE_NOT_FOUND));
	}

	private void checkIsSaleExist(Long saleId) {
		if (saleRepository.countSaleById(saleId) == 0) {
			throw new LetsDealAppException(ErrorCode.SALE_NOT_FOUND);
		}
	}

	private Proposal getProposalOrException(Long proposalId) {
		return proposalRepository.findById(proposalId).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.PROPOSAL_NOT_FOUND));
	}

	private List<ImageDto> getImageOrException(Long saleId) {
		List<ImageDto> images = imageRepository.findAllBySaleIdAndOrderBySortOrderAsc(saleId)
			.stream().map(ImageDto::from).collect(Collectors.toList());

		if (ObjectUtils.isEmpty(images)) {
			throw new LetsDealAppException(ErrorCode.IMAGE_NOT_FOUND);
		}

		return images;
	}

	private void uploadImages(List<MultipartFile> imageFiles, Sale sale) throws IOException {
		if (imageFiles != null && !imageFiles.isEmpty()) {
			int order = 1;

			for (MultipartFile file : imageFiles) {
				// String imageUrl = awsS3Service.saveImageToS3(file);
				String imageUrl = "dummy";
				sale.addImage(
					Image.builder(sale, imageUrl, order++)
						.build()
				);
			}
		} else {
			throw new LetsDealAppException(ErrorCode.EMPTY_IMAGE);
		}
	}

}
