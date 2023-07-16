package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.repository.SaleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;

    @MockBean
    private SaleRepository saleRepository;

    @Test
    void 판매글목록조회_성공(){
        String userName = "SOME_USERNAME";
        SearchCondition condition = mock(SearchCondition.class);
        Pageable pageable = mock(Pageable.class);
        when(saleRepository.findAllBySearchCondition(condition, pageable)).thenReturn(Page.empty());

        Assertions.assertDoesNotThrow(() -> saleService.getSaleList(condition, pageable, userName));
    }
}
