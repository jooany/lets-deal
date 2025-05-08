package com.jooany.letsdeal.service;

import static com.jooany.letsdeal.fixture.dto.DtoFixture.*;
import static com.jooany.letsdeal.fixture.entity.EntityFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.ProposalListRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.fixture.dto.DtoFixture;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Proposal;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.CategoryRepository;
import com.jooany.letsdeal.repository.ImageRepository;
import com.jooany.letsdeal.repository.ProposalRepository;
import com.jooany.letsdeal.repository.SaleRepository;
import com.jooany.letsdeal.repository.UserRepository;

/*
    서비스는 통합테스트에서 단위테스트로 변경
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class SaleServiceTest {
	@Mock
	private SaleRepository saleRepository;

	@Mock
	private ImageRepository imageRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CategoryRepository categoryRepository;

	// @Mock
	// private AwsS3Service awsS3Service;

	@Mock
	private ProposalRepository proposalRepository;

	@InjectMocks
	private SaleService saleService;

	@DisplayName("전체 판매글 목록 조회 - 성공")
	@Test
	void getSaleList() {
		String userName = "testUser";
		SearchCondition searchCondition = mock(SearchCondition.class);
		Pageable pageable = PageRequest.of(0, 10);

		List<SaleRes> sales = createSaleInfoResList();
		Page<SaleRes> salePages = new PageImpl<>(sales, pageable, sales.size());

		given(saleRepository.findAllBySearchCondition(searchCondition, pageable)).willReturn(salePages);

		Page<SaleRes> result = saleService.getSaleList(searchCondition, pageable, userName);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEqualTo(sales);
		assertThat(result.getTotalElements()).isEqualTo(sales.size());
		assertThat(result.getTotalPages()).isEqualTo(1);
		verify(saleRepository, times(1)).findAllBySearchCondition(searchCondition, pageable);
	}

	@DisplayName("판매글 목록 키워드 검색 조회 - 성공")
	@Test
	void getSaleListByKeyword() {
		String userName = "testUser";
		SearchCondition searchCondition = createKeywordSearchCondition();
		Pageable pageable = PageRequest.of(0, 10);

		List<SaleRes> sales = createSaleInfoResList();
		Page<SaleRes> salePages = new PageImpl<>(sales, pageable, sales.size());

		given(saleRepository.findAllBySearchCondition(searchCondition, pageable)).willReturn(salePages);

		Page<SaleRes> result = saleService.getSaleList(searchCondition, pageable, userName);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).isEqualTo(sales);
		assertThat(result.getTotalElements()).isEqualTo(sales.size());
		assertThat(result.getTotalPages()).isEqualTo(1);
		verify(saleRepository, times(1)).findAllBySearchCondition(searchCondition, pageable);
	}

	@DisplayName("특정 id의 판매글 조회 - 성공")
	@Test
	void getSaleInfo() {

		Sale sale = createSale();
		SaleInfoRes saleInfoRes = createSaleInfoRes();
		Long id = saleInfoRes.getId();
		given(saleRepository.findSaleInfoResById(id)).willReturn(Optional.of(saleInfoRes));
		given(imageRepository.findAllBySaleIdAndOrderBySortOrderAsc(id)).willReturn(sale.getImages());

		SaleInfoRes saleInfo = saleService.getSaleInfo(id);

		assertThat(saleInfo.getId()).isEqualTo(saleInfoRes.getId());
		assertThat(saleInfo.getUserId()).isEqualTo(saleInfoRes.getUserId());
		assertThat(saleInfo.getUserName()).isEqualTo(saleInfoRes.getUserName());
		assertThat(saleInfo.getCategoryId()).isEqualTo(saleInfoRes.getCategoryId());
		assertThat(saleInfo.getCategoryName()).isEqualTo(saleInfoRes.getCategoryName());
		assertThat(saleInfo.getImages()).isEqualTo(saleInfoRes.getImages());
		assertThat(saleInfo.getMaxBuyerPrice()).isEqualTo(saleInfoRes.getMaxBuyerPrice());
		assertThat(saleInfo.getTitle()).isEqualTo(saleInfoRes.getTitle());
		assertThat(saleInfo.getContents()).isEqualTo(saleInfoRes.getContents());
		assertThat(saleInfo.getSellerPrice()).isEqualTo(saleInfoRes.getSellerPrice());
		assertThat(saleInfo.getSaleStatus()).isEqualTo(saleInfoRes.getSaleStatus());
		assertThat(saleInfo.getRegisteredAt()).isEqualTo(saleInfoRes.getRegisteredAt());
		assertThat(saleInfo.getUpdateAt()).isEqualTo(saleInfoRes.getUpdateAt());
		verify(saleRepository, times(1)).findSaleInfoResById(id);
		verify(imageRepository, times(1)).findAllBySaleIdAndOrderBySortOrderAsc(id);
	}

	@DisplayName("특정 id의 판매글 조회 - 실패 (판매글이 존재하지 않음)")
	@Test
	void getSaleInfoWhenSaleNotFound() {
		Long nonExistingSaleId = 999L;
		given(saleRepository.findSaleInfoResById(nonExistingSaleId)).willReturn(Optional.empty());

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> saleService.getSaleInfo(nonExistingSaleId));
		assertEquals(ErrorCode.SALE_NOT_FOUND, e.getErrorCode());

		verify(saleRepository, times(1)).findSaleInfoResById(nonExistingSaleId);
		verify(imageRepository, never()).findAllBySaleIdAndOrderBySortOrderAsc(any());
	}

	@DisplayName("판매글 저장_이미지가 1~3장인 경우 - 성공")
	@Test
	void saveSale() throws IOException {
		SaleSaveReq saleSaveReq = createSaleSaveReq();
		User user = EntityFixture.createUser();
		Category category = createCategory();
		given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
		given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

		List<MultipartFile> imageFiles = createImageFiles();
		// given(awsS3Service.saveImageToS3(imageFiles.get(0))).willReturn(
		// 	"https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG");
		// given(awsS3Service.saveImageToS3(imageFiles.get(1))).willReturn(
		// 	"https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG");

		saleService.saveSale(saleSaveReq, imageFiles, user.getUserName());

		verify(userRepository, times(1)).findByUserName(user.getUserName());
		verify(categoryRepository, times(1)).findById(category.getId());
		verify(saleRepository, times(1)).save(any(Sale.class));
		// verify(awsS3Service, times(2)).saveImageToS3(any());
	}

	@DisplayName("판매글 저장_이미지가 없는 경우 - 실패")
	@Test
	void saveSaleWithoutImages() throws IOException {
		SaleSaveReq saleSaveReq = createSaleSaveReq();
		User user = EntityFixture.createUser();
		Category category = createCategory();
		given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
		given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

		List<MultipartFile> imageFiles = Collections.emptyList();

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> saleService.saveSale(saleSaveReq, imageFiles, user.getUserName()));
		assertEquals(ErrorCode.EMPTY_IMAGE, e.getErrorCode());
		verify(userRepository, times(1)).findByUserName(user.getUserName());
		verify(categoryRepository, times(1)).findById(category.getId());
		verify(saleRepository, never()).save(any(Sale.class));
		// verify(awsS3Service, never()).saveImageToS3(any());
	}

	@DisplayName("판매글 수정 - 성공")
	@Test
	void updateSale() throws IOException {
		SaleSaveReq saleSaveReq = createSaleSaveReq();
		Sale sale = EntityFixture.createSale();
		Long saleId = sale.getId();
		UserDto userDto = DtoFixture.createUserDto();
		Category category = createCategory();
		List<MultipartFile> imageFiles = createImageFiles();
		// String imageUrl = "https://letsdeal-bucket/sales/update_image.JPG";

		given(saleRepository.findById(sale.getId())).willReturn(Optional.of(sale));
		given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
		// given(awsS3Service.saveImageToS3(any(MultipartFile.class))).willReturn(imageUrl);

		saleService.updateSale(saleId, userDto.getId(), userDto.getUserRole(), saleSaveReq, imageFiles);

		verify(saleRepository, times(1)).findById(saleId);
		verify(categoryRepository, times(1)).findById(category.getId());
		verify(imageRepository, times(2)).delete(any(Image.class));
		// verify(awsS3Service, times(2)).deleteImage(anyString());
		// verify(awsS3Service, times(2)).saveImageToS3(any());
		assertEquals(category, sale.getCategory());
		assertEquals(saleSaveReq.getTitle(), sale.getTitle());
		assertEquals(saleSaveReq.getContents(), sale.getContents());
		assertEquals(saleSaveReq.getSellerPrice(), sale.getSellerPrice());
		// assertEquals(imageUrl, sale.getImages().get(0).getImageUrl());
		// assertEquals(imageUrl, sale.getImages().get(1).getImageUrl());
	}

	@DisplayName("판매글 수정_로그인 사용자가 판매글 작성자가 아닌 사용자인 경우 - 실패")
	@Test
	void updateSale_invalidPermission() throws IOException {
		SaleSaveReq saleSaveReq = createSaleSaveReq();
		Sale sale = EntityFixture.createSale();
		Long saleId = sale.getId();
		UserDto userDto = DtoFixture.createUserDto(999L);
		Category category = createCategory();
		List<MultipartFile> imageFiles = createImageFiles();

		given(saleRepository.findById(sale.getId())).willReturn(Optional.of(sale));
		given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> saleService.updateSale(saleId, userDto.getId(), userDto.getUserRole(), saleSaveReq, imageFiles));
		assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);

		verify(saleRepository, times(1)).findById(saleId);
		verify(categoryRepository, times(1)).findById(category.getId());
	}

	@DisplayName("판매글 삭제 - 성공")
	@Test
	void deleteSale() {
		Sale sale = EntityFixture.createSale();
		User user = sale.getWriter();
		Long saleId = sale.getId();
		String userName = user.getUserName();

		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
		given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));

		saleService.deleteSale(saleId, userName);

		verify(userRepository, times(1)).findByUserName(userName);
		verify(saleRepository, times(1)).findById(saleId);
		verify(saleRepository, times(1)).softDeleteById(saleId);
		verify(imageRepository, times(1)).deleteAllBySale(sale);
		// verify(awsS3Service, atLeastOnce()).deleteImage(anyString());
		verify(proposalRepository, times(1)).deleteAllBySale(sale);
	}

	@DisplayName("가격제안 목록 조회 - 성공")
	@Test
	@WithMockUser
	void getProposalList() {
		Long saleId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		String userName = "testUser";
		List<ProposalRes> proposals = DtoFixture.createProposalResList();
		Page<ProposalRes> proposalPages = new PageImpl<>(proposals, pageable, proposals.size());
		List<MyProposalRes> myProposals = DtoFixture.createMyProposalResList();
		given(saleRepository.countSaleById(saleId)).willReturn(1L);
		given(proposalRepository.findAllBySaleId(saleId, userName, pageable)).willReturn(proposalPages);
		given(proposalRepository.findAllBySaleIdAndUserName(saleId, userName)).willReturn(myProposals);

		ProposalListRes result = saleService.getProposalList(saleId, pageable, userName);

		assertThat(result).isNotNull();
		assertThat(result.getProposalList().getContent()).isEqualTo(proposals);
		verify(saleRepository, times(1)).countSaleById(saleId);
		verify(proposalRepository, times(1)).findAllBySaleId(saleId, userName, pageable);
	}

	@DisplayName("가격제안 등록 - 성공")
	@Test
	@WithMockUser
	void saveProposal() {
		Long saleId = 1L;
		Integer buyerPrice = 5000;
		String userName = "testUser";
		Sale sale = EntityFixture.createSale();
		User writer = sale.getWriter();
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(writer));
		given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));

		saleService.saveProposal(1L, buyerPrice, userName);

		verify(userRepository, times(1)).findByUserName(userName);
		verify(saleRepository, times(1)).findById(saleId);
		verify(proposalRepository, times(1)).save(any(Proposal.class));
	}

	@DisplayName("가격제안 삭제 - 성공")
	@Test
	@WithMockUser
	void deleteProposal() {
		Long saleId = 1L;
		Long proposalId = 1L;
		String userName = "testUser";
		Sale sale = EntityFixture.createSale();
		Proposal proposal = EntityFixture.createProposal(sale);
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(proposal.getBuyer()));
		given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));
		given(proposalRepository.findById(proposalId)).willReturn(Optional.of(proposal));

		saleService.deleteProposal(1L, 1L, userName);

		verify(userRepository, times(1)).findByUserName(userName);
		verify(saleRepository, times(1)).findById(saleId);
		verify(proposalRepository, times(1)).delete(proposal);
	}

	@DisplayName("가격제안 삭제_등록자가 아닌 사용자인 경우 - 실패")
	@Test
	@WithMockUser
	void deleteProposal_invalidPermission() {
		Long proposalId = 1L;
		String userName = "testUser";
		User anotherUser = EntityFixture.createUser();
		Sale sale = EntityFixture.createSale();
		Proposal proposal = EntityFixture.createProposal(sale);
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(anotherUser));
		given(proposalRepository.findById(proposalId)).willReturn(Optional.of(proposal));

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> saleService.deleteProposal(1L, 1L, userName));
		assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

		verify(userRepository, times(1)).findByUserName(userName);
		verify(proposalRepository, never()).delete(proposal);
	}

	@DisplayName("가격제안 거절 - 성공")
	@Test
	@WithMockUser
	void refuseProposal() {
		Long saleId = 1L;
		Long proposalId = 1L;
		String userName = "testUser";
		Sale sale = EntityFixture.createSale();
		Proposal proposal = EntityFixture.createProposal(sale);
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(sale.getWriter()));
		given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));
		given(proposalRepository.findById(proposalId)).willReturn(Optional.of(proposal));

		saleService.refuseProposal(1L, 1L, userName);

		verify(userRepository, times(1)).findByUserName(userName);
		verify(saleRepository, times(1)).findById(saleId);
		verify(proposalRepository, times(1)).findById(proposalId);
		verify(proposalRepository, times(1)).save(proposal);
	}

	@DisplayName("가격제안 거절_로그인 사용자가 판매글 작성자가 아닌 경우 - 실패")
	@Test
	@WithMockUser
	void refuseProposal_invalidPermission() {
		Long saleId = 1L;
		Long proposalId = 1L;
		String userName = "testUser";
		Sale sale = EntityFixture.createSale();
		User anotherUser = EntityFixture.createUser();
		Proposal proposal = EntityFixture.createProposal(sale);
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(anotherUser));
		given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> saleService.refuseProposal(1L, 1L, userName));
		assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());

		verify(userRepository, times(1)).findByUserName(userName);
		verify(saleRepository, times(1)).findById(saleId);
		verify(proposalRepository, never()).findById(proposalId);
		verify(proposalRepository, never()).save(proposal);
	}

	private List<MultipartFile> createImageFiles() {
		List<MultipartFile> imageFiles = new ArrayList<>();
		MockMultipartFile multipartFile1 = new MockMultipartFile("test", "test.png", MediaType.IMAGE_PNG_VALUE,
			"test".getBytes());
		MockMultipartFile multipartFile2 = new MockMultipartFile("test2", "test2.png", MediaType.IMAGE_PNG_VALUE,
			"test2".getBytes());
		imageFiles.add(multipartFile1);
		imageFiles.add(multipartFile2);
		return imageFiles;
	}

}
