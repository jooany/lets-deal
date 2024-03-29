package com.jooany.letsdeal.repository;

import static com.jooany.letsdeal.model.entity.QImage.*;

import java.util.List;

import com.jooany.letsdeal.model.entity.Image;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImageCustomRepositoryImpl implements ImageCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Image> findAllBySaleIdAndOrderBySortOrderAsc(Long saleId) {
		return queryFactory
			.selectFrom(image)
			.where(image.sale.id.eq(saleId))
			.orderBy(image.sortOrder.asc())
			.fetch();
	}

}
