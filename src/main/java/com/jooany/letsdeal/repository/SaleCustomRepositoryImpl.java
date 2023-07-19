package com.jooany.letsdeal.repository;

import com.google.common.base.Enums;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.QSaleListRes;
import com.jooany.letsdeal.controller.dto.response.QSaleRes;
import com.jooany.letsdeal.controller.dto.response.SaleListRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.jooany.letsdeal.model.entity.QImage.image;
import static com.jooany.letsdeal.model.entity.QProposal.proposal;
import static com.jooany.letsdeal.model.entity.QSale.sale;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class SaleCustomRepositoryImpl implements SaleCustomRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<SaleListRes> findAllBySearchCondition(SearchCondition searchCondition, Pageable pageable) {

        JPQLQuery<SaleListRes> query = queryFactory
                .select(new QSaleListRes(
                        sale.id,
                        image.imageUrl,
                        sale.title,
                        sale.sellerPrice,
                        sale.saleStatus,
                        proposal.buyerPrice.max(),
                        sale.registeredAt,
                        sale.updateAt
                        )
                )
                .from(sale)
                .join(image).on(image.sale.eq(sale).and(image.sortOrder.eq(1)))
                .leftJoin(proposal).on(proposal.sale.eq(sale))
                .where(
                        saleStatusEq(searchCondition.getSaleStatus()),
                        keywordEq(searchCondition.getKeyword()),
                        categoryIdEq(searchCondition.getCategoryId()),
                        userNameEq(searchCondition.getTargetedUserName(), searchCondition.getCurrentUserName(), searchCondition.getIsCurrentUserSale(), searchCondition.getIsCurrentUserOfferedProposal())
                )
                .groupBy(sale.id, image.imageUrl, sale.title, sale.sellerPrice, sale.saleStatus, sale.registeredAt, sale.updateAt);

        QueryResults<SaleListRes> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Optional<SaleRes> findSaleResById(Long saleId) {
        return Optional.ofNullable(queryFactory
                .select(
                        new QSaleRes(
                                sale.id,
                                sale.user.id,
                                sale.user.userName,
                                sale.category.id,
                                sale.category.categoryName,
                                proposal.buyerPrice.max(),
                                sale.title,
                                sale.contents,
                                sale.sellerPrice,
                                sale.saleStatus,
                                sale.registeredAt,
                                sale.updateAt
                        ))
                .from(sale)
                .leftJoin(proposal).on(proposal.sale.eq(sale))
                .where(sale.id.eq(saleId))
                .groupBy(sale.id, sale.user.id, sale.user.userName, sale.category.id, sale.category.categoryName, sale.title, sale.contents, sale.sellerPrice, sale.saleStatus, sale.registeredAt, sale.updateAt)
                .fetchOne());
    }

    private BooleanExpression saleStatusEq(SaleStatus saleStatus) {
        if(saleStatus != null){
            if(Enums.getIfPresent(SaleStatus.class, saleStatus.toString()).isPresent()){
                return sale.saleStatus.eq(saleStatus);
            }
        }

        return null;
    }

    private BooleanExpression keywordEq(String keyword) {
        return hasText(keyword) ? sale.title.containsIgnoreCase(keyword).or(sale.contents.containsIgnoreCase(keyword)) : null;
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? sale.category.id.eq(categoryId) : null;
    }

    private BooleanExpression userNameEq(String targetedUserName, String currentUserName, Boolean isCurrentUserSale, Boolean isCurrentUserOfferedProposal) {
        if(hasText(targetedUserName)){
            return sale.user.userName.eq(targetedUserName);
        }

        if(isCurrentUserSale != null && isCurrentUserSale){
            return sale.user.userName.eq(currentUserName);
        }

        if(isCurrentUserOfferedProposal != null && isCurrentUserOfferedProposal){
            return proposal.user.userName.eq(currentUserName);
        }

        return null;
    }

}
