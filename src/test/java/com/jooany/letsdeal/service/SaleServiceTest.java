package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.request.SaleSaveReq;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.Category;
import com.jooany.letsdeal.model.entity.Image;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.*;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.jooany.letsdeal.fixture.dto.DtoFixture.*;
import static com.jooany.letsdeal.fixture.entity.EntityFixture.createCategory;
import static com.jooany.letsdeal.fixture.entity.EntityFixture.createSale;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/*
    서비스는 통합테스트에서 단위테스트로 변경
 */
@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {
    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AwsS3Service awsS3Service;

    @Mock
    private ProposalRepository proposalRepository;

    @InjectMocks
    private SaleService saleService;


    @DisplayName("전체 판매글 목록 조회 - 성공")
    @Test
    void getSaleList(){
        String userName = "testUser";
        SearchCondition searchCondition = mock(SearchCondition.class);
        Pageable pageable = PageRequest.of(0, 10);

        List<SaleListRes> sales = createSaleResList();
        Page<SaleListRes> salePages = new PageImpl<>(sales, pageable, sales.size());

        given(saleRepository.findAllBySearchCondition(searchCondition, pageable)).willReturn(salePages);

        Page<SaleListRes> result = saleService.getSaleList(searchCondition, pageable, userName);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(sales);
        assertThat(result.getTotalElements()).isEqualTo(sales.size());
        assertThat(result.getTotalPages()).isEqualTo(1);
        verify(saleRepository, times(1)).findAllBySearchCondition(searchCondition, pageable);
    }

    @DisplayName("판매글 목록 키워드 검색 조회 - 성공")
    @Test
    void getSaleListByKeyword(){
        String userName = "testUser";
        SearchCondition searchCondition = createKeywordSearchCondition();
        Pageable pageable = PageRequest.of(0, 10);

        List<SaleListRes> sales = createSaleResList();
        Page<SaleListRes> salePages = new PageImpl<>(sales, pageable, sales.size());

        given(saleRepository.findAllBySearchCondition(searchCondition, pageable)).willReturn(salePages);

        Page<SaleListRes> result = saleService.getSaleList(searchCondition, pageable, userName);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo(sales);
        assertThat(result.getTotalElements()).isEqualTo(sales.size());
        assertThat(result.getTotalPages()).isEqualTo(1);
        verify(saleRepository, times(1)).findAllBySearchCondition(searchCondition, pageable);
    }

    @DisplayName("특정 id의 판매글 조회 - 성공")
    @Test
    void getSaleInfo(){

        Sale sale = createSale();
        SaleRes saleRes = createSaleRes();
        Long id = saleRes.getId();
        given(saleRepository.findSaleResById(id)).willReturn(Optional.of(saleRes));
        given(imageRepository.findAllBySaleIdAndOrderBySortOrderAsc(id)).willReturn(sale.getImages());

        SaleRes saleInfo = saleService.getSaleInfo(id);

        assertThat(saleInfo.getId()).isEqualTo(saleRes.getId());
        assertThat(saleInfo.getUserId()).isEqualTo(saleRes.getUserId());
        assertThat(saleInfo.getUserName()).isEqualTo(saleRes.getUserName());
        assertThat(saleInfo.getCategoryId()).isEqualTo(saleRes.getCategoryId());
        assertThat(saleInfo.getCategoryName()).isEqualTo(saleRes.getCategoryName());
        assertThat(saleInfo.getImages()).isEqualTo(saleRes.getImages());
        assertThat(saleInfo.getMaxBuyerPrice()).isEqualTo(saleRes.getMaxBuyerPrice());
        assertThat(saleInfo.getTitle()).isEqualTo(saleRes.getTitle());
        assertThat(saleInfo.getContents()).isEqualTo(saleRes.getContents());
        assertThat(saleInfo.getSellerPrice()).isEqualTo(saleRes.getSellerPrice());
        assertThat(saleInfo.getSaleStatus()).isEqualTo(saleRes.getSaleStatus());
        assertThat(saleInfo.getRegisteredAt()).isEqualTo(saleRes.getRegisteredAt());
        assertThat(saleInfo.getUpdateAt()).isEqualTo(saleRes.getUpdateAt());
        verify(saleRepository, times(1)).findSaleResById(id);
        verify(imageRepository, times(1)).findAllBySaleIdAndOrderBySortOrderAsc(id);
    }

    @DisplayName("특정 id의 판매글 조회 - 실패 (판매글이 존재하지 않음)")
    @Test
    void getSaleInfoWhenSaleNotFound() {
        // 존재하지 않는 판매글 id를 사용하여 조회 시도
        Long nonExistingSaleId = 999L;
        given(saleRepository.findSaleResById(nonExistingSaleId)).willReturn(Optional.empty());

        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> saleService.getSaleInfo(nonExistingSaleId));
        assertEquals(ErrorCode.SALE_NOT_FOUND, e.getErrorCode());

        verify(saleRepository, times(1)).findSaleResById(nonExistingSaleId);
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
        given(awsS3Service.saveImageToS3(imageFiles.get(0))).willReturn("https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG");
        given(awsS3Service.saveImageToS3(imageFiles.get(1))).willReturn("https://letsdeal-bucket.s3.ap-northeast-2.amazonaws.com/sales/7c18e420-b218-440e-a8af-8df76d6ee223.JPG");

        saleService.saveSale(saleSaveReq, imageFiles, user.getUserName());

        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(saleRepository, times(1)).save(any(Sale.class));
        verify(awsS3Service, times(2)).saveImageToS3(any());
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

        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> saleService.saveSale(saleSaveReq, imageFiles, user.getUserName()));
        assertEquals(ErrorCode.EMPTY_IMAGE, e.getErrorCode());
        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(saleRepository, never()).save(any(Sale.class));
        verify(awsS3Service, never()).saveImageToS3(any());
    }

    @DisplayName("판매글 수정 - 성공")
    @Test
    void updateSale() throws IOException {
        SaleSaveReq saleSaveReq = createSaleSaveReq();
        Sale sale = EntityFixture.createSale();
        Long saleId = sale.getId();
        User user = sale.getUser();
        Category category = createCategory();
        List<MultipartFile> imageFiles = createImageFiles();
        String imageUrl = "https://letsdeal-bucket/sales/update_image.JPG";

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));
        given(saleRepository.findById(sale.getId())).willReturn(Optional.of(sale));
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        given(awsS3Service.saveImageToS3(any(MultipartFile.class))).willReturn(imageUrl);

        saleService.updateSale(saleId, saleSaveReq, imageFiles, user.getUserName());

        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(saleRepository, times(1)).findById(saleId);
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(imageRepository, times(2)).delete(any(Image.class));
        verify(awsS3Service, times(2)).deleteImage(anyString());
        verify(awsS3Service, times(2)).saveImageToS3(any());
        assertEquals(category, sale.getCategory());
        assertEquals(saleSaveReq.getTitle(), sale.getTitle());
        assertEquals(saleSaveReq.getContents(), sale.getContents());
        assertEquals(saleSaveReq.getSellerPrice(), sale.getSellerPrice());
        assertEquals(imageUrl, sale.getImages().get(0).getImageUrl());
        assertEquals(imageUrl, sale.getImages().get(1).getImageUrl());
    }

    @DisplayName("판매글 수정_로그인 사용자가 판매글 작성자가 아닌 사용자인 경우 - 실패")
    @Test
    void updateSale_invalidPermission() throws IOException {
        SaleSaveReq saleSaveReq = createSaleSaveReq();
        Sale sale = EntityFixture.createSale();
        Long saleId = sale.getId();
        User user = EntityFixture.createUser("anotherUser", "password");
        Category category = createCategory();
        List<MultipartFile> imageFiles = createImageFiles();

        given(userRepository.findByUserName("anotherUser")).willReturn(Optional.of(user));
        given(saleRepository.findById(sale.getId())).willReturn(Optional.of(sale));
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class, () -> saleService.updateSale(saleId, saleSaveReq, imageFiles, user.getUserName()));
        assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);

        verify(userRepository, times(1)).findByUserName(user.getUserName());
        verify(saleRepository, times(1)).findById(saleId);
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @DisplayName("판매글 삭제 - 성공")
    @Test
    void deleteSale(){
        Sale sale = EntityFixture.createSale();
        User user = sale.getUser();
        Long saleId = sale.getId();
        String userName = user.getUserName();

        given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
        given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));

        saleService.deleteSale(saleId, userName);

        verify(userRepository, times(1)).findByUserName(userName);
        verify(saleRepository, times(1)).findById(saleId);
        verify(saleRepository, times(1)).deleteById(saleId);
        verify(imageRepository, times(1)).deleteAllBySale(sale);
        verify(awsS3Service, atLeastOnce()).deleteImage(anyString());
        verify(proposalRepository, times(1)).deleteAllBySale(sale);
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
