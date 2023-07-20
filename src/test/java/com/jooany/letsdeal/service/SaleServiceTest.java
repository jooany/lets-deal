package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.repository.ImageRepository;
import com.jooany.letsdeal.repository.SaleRepository;
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

import java.util.List;

import static com.jooany.letsdeal.fixture.dto.DtoFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
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

        Sale sale = EntityFixture.createSale();
        SaleRes saleRes = createSaleRes();
        Long id = saleRes.getId();
        given(saleRepository.findSaleResById(id)).willReturn(java.util.Optional.of(saleRes));
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
    }
    

}
