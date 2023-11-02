package com.jooany.letsdeal.repository;

import static com.jooany.letsdeal.model.entity.QImage.*;
import static com.jooany.letsdeal.model.entity.QProposal.*;
import static com.jooany.letsdeal.model.entity.QSale.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.google.common.base.Enums;
import com.jooany.letsdeal.controller.dto.request.SearchCondition;
import com.jooany.letsdeal.controller.dto.response.QSaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.QSaleRes;
import com.jooany.letsdeal.controller.dto.response.SaleInfoRes;
import com.jooany.letsdeal.controller.dto.response.SaleRes;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaleCustomRepositoryImpl implements SaleCustomRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<SaleRes> findAllBySearchCondition(SearchCondition searchCondition, Pageable pageable) {

		List<SaleRes> result = queryFactory
			.select(new QSaleRes(
					sale.id,
					image.imageUrl,
					sale.title,
					sale.sellerPrice,
					sale.saleStatus,
					proposal.buyerPrice,
					new CaseBuilder()
						.when(sale.user.userName.eq(searchCondition.getCurrentUserName()))
						.then(true)
						.otherwise(false).as("isSeller"),
					sale.registeredAt,
					sale.updateAt
				)
			)
			.from(sale)
			.join(image).on(image.sale.eq(sale).and(image.sortOrder.eq(1)))
			.leftJoin(proposal).on(proposal.sale.eq(sale).and(proposal.id.eq(sale.maxPriceProposal.id)))
			.where(
				saleStatusEq(searchCondition.getSaleStatus()),
				keywordEq(searchCondition.getKeyword()),
				categoryIdEq(searchCondition.getCategoryId()),
				userNameEq(searchCondition.getTargetedUserName(), searchCondition.getCurrentUserName(),
					searchCondition.getIsCurrentUserSale(),
					searchCondition.getIsCurrentUserOfferedProposal())
			)
			.orderBy(sale.registeredAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long count = queryFactory
			.select(sale.count())
			.from(sale)
			.where(
				saleStatusEq(searchCondition.getSaleStatus()),
				keywordEq(searchCondition.getKeyword()),
				categoryIdEq(searchCondition.getCategoryId()),
				userNameEq(searchCondition.getTargetedUserName(), searchCondition.getCurrentUserName(),
					searchCondition.getIsCurrentUserSale(),
					searchCondition.getIsCurrentUserOfferedProposal())
			).fetchOne();

		return new PageImpl<>(result, pageable, count);
	}

	@Override
	public Optional<SaleInfoRes> findSaleInfoResById(Long saleId) {
		return Optional.ofNullable(queryFactory
			.select(
				new QSaleInfoRes(
					sale.id,
					sale.user.id,
					sale.user.userName,
					sale.category.id,
					sale.category.categoryName,
					proposal.buyerPrice,
					sale.title,
					sale.contents,
					sale.sellerPrice,
					sale.saleStatus,
					sale.registeredAt,
					sale.updateAt
				))
			.from(sale)
			.leftJoin(proposal).on(proposal.sale.eq(sale).and(proposal.id.eq(sale.maxPriceProposal.id)))
			.where(sale.id.eq(saleId))
			.fetchOne());
	}

	private BooleanExpression saleStatusEq(SaleStatus saleStatus) {
		if (saleStatus != null) {
			if (Enums.getIfPresent(SaleStatus.class, saleStatus.toString()).isPresent()) {
				return sale.saleStatus.eq(saleStatus);
			}
		}

		return null;
	}

	private BooleanExpression keywordEq(String keyword) {
		return hasText(keyword) ? sale.title.containsIgnoreCase(keyword).or(sale.contents.containsIgnoreCase(keyword))
			: null;
	}

	private BooleanExpression categoryIdEq(Long categoryId) {
		return categoryId != null ? sale.category.id.eq(categoryId) : null;
	}

	private BooleanExpression userNameEq(String targetedUserName, String currentUserName, Boolean isCurrentUserSale,
		Boolean isCurrentUserOfferedProposal) {
		if (hasText(targetedUserName)) {
			return sale.user.userName.eq(targetedUserName);
		}

		if (isCurrentUserSale != null && isCurrentUserSale) {
			return sale.user.userName.eq(currentUserName);
		}

		if (isCurrentUserOfferedProposal != null && isCurrentUserOfferedProposal) {
			return proposal.user.userName.eq(currentUserName);
		}

		return null;
	}

}
