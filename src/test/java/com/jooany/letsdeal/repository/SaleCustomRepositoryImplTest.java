//package com.jooany.letsdeal.repository;
//
//import com.jooany.letsdeal.controller.dto.request.SearchCondition;
//import com.jooany.letsdeal.controller.dto.response.SaleListRes;
//import com.jooany.letsdeal.model.entity.QImage;
//import com.jooany.letsdeal.model.entity.QProposal;
//import com.jooany.letsdeal.model.entity.QSale;
//import com.jooany.letsdeal.model.enumeration.SaleStatus;
//import com.querydsl.core.QueryResults;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//import java.util.List;
//
//class SaleCustomRepositoryImplTest {
//    @Mock
//    private JPAQueryFactory queryFactory;
//
//    @InjectMocks
//    private SaleCustomRepositoryImpl saleCustomRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void findAllBySearchCondition_WhenValidConditionAndPageable_ReturnsExpectedPage() {
//        // Mock search condition and pageable
//        SearchCondition searchCondition = new SearchCondition();
//        searchCondition.setSaleStatus(SaleStatus.SELLING);
//        searchCondition.setKeyword("example");
//        searchCondition.setCategoryId(1L);
//        searchCondition.setTargetedUserName("user1");
//        searchCondition.setCurrentUserName("user2");
//        searchCondition.setIsCurrentUserSale(true);
//        searchCondition.setIsCurrentUserOfferedProposal(false);
//
//        Pageable pageable = Pageable.ofSize(10).withSort(Sort.by("registeredAt").descending());
//
//        // Mock entities and results
//        QSale sale = QSale.sale;
//        QImage image = QImage.image;
//        QProposal proposal = QProposal.proposal;
//
//        SaleListRes saleListRes1 = new SaleListRes(1L, "image1.jpg", "Title 1", 100, SaleStatus.SELLING, 500, null, null);
//        SaleListRes saleListRes2 = new SaleListRes(2L, "image2.jpg", "Title 2", 200, SaleStatus.SELLING, 300, null, null);
//        List<SaleListRes> expectedResults = List.of(saleListRes1, saleListRes2);
//        long totalResults = expectedResults.size();
//
//        // Mock query and fetch results
//        when(queryFactory
//                .select(any())
//                .from(any(QSale.class))
//                .join(any(QImage.class), any())
//                .leftJoin(any(QProposal.class), any())
//                .where(any(BooleanExpression.class))
//                .groupBy(anyVararg())
//                .offset(anyLong())
//                .limit(anyInt())
//                .fetchResults()
//        ).thenReturn(new QueryResults<>(expectedResults, pageable, totalResults));
//
//        // Perform the query
//        Page<SaleListRes> actualPage = saleCustomRepository.findAllBySearchCondition(searchCondition, pageable);
//
//        // Assertions
//        assertEquals(expectedResults, actualPage.getContent());
//        assertEquals(totalResults, actualPage.getTotalElements());
//        assertEquals(pageable, actualPage.getPageable());
//    }
//
//}