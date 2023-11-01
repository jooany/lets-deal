package com.jooany.letsdeal.repository;

import static com.jooany.letsdeal.model.entity.QProposal.*;
import static com.jooany.letsdeal.model.entity.QSale.*;
import static com.jooany.letsdeal.model.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.jooany.letsdeal.controller.dto.MyProposalRes;
import com.jooany.letsdeal.controller.dto.QMyProposalRes;
import com.jooany.letsdeal.controller.dto.response.ProposalRes;
import com.jooany.letsdeal.controller.dto.response.QProposalRes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProposalCustomRepositoryImpl implements ProposalCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<ProposalRes> findAllBySaleId(Long saleId, String userName, Pageable pageable) {

		BooleanExpression isProposedByCurrentUser = proposal.user.userName.eq(userName);

		List<ProposalRes> result =
			queryFactory
				.select(new QProposalRes(
					proposal.id,
					sale.id,
					user.id,
					user.userName,
					proposal.buyerPrice,
					proposal.proposalStatus,
					isProposedByCurrentUser.as("isProposedByCurrentUser"),
					proposal.registeredAt
				))
				.from(proposal)
				.join(sale).on(sale.id.eq(proposal.sale.id).and(sale.id.eq(saleId)))
				.join(user).on(user.id.eq(proposal.user.id))
				.orderBy(proposal.buyerPrice.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		Long count = queryFactory
			.select(proposal.count())
			.from(proposal)
			.join(sale).on(sale.id.eq(proposal.sale.id).and(sale.id.eq(saleId)))
			.join(user).on(user.id.eq(proposal.user.id))
			.fetchOne();

		return new PageImpl<>(result, pageable, count);
	}

	@Override
	public List<MyProposalRes> findAllBySaleIdAndUserName(Long saleId, String userName) {
		return queryFactory
			.select(new QMyProposalRes(
				proposal.id,
				sale.id,
				proposal.buyerPrice,
				proposal.proposalStatus,
				proposal.registeredAt
			))
			.from(proposal)
			.join(sale).on(sale.id.eq(proposal.sale.id).and(sale.id.eq(saleId)))
			.join(user).on(user.id.eq(proposal.user.id).and(user.userName.eq(userName)))
			.orderBy(proposal.registeredAt.desc())
			.fetch();
	}

}
