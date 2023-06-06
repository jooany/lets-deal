package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.SaleDto;
import com.jooany.letsdeal.controller.dto.SaleWithMaxBuyerPriceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class SaleCustomRepositoryImpl implements SaleCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SaleWithMaxBuyerPriceDto> findSaleWithMaxBuyerPrice(SaleDto saleDto, Integer maxBuyerPrice) {
        return null;
    }
}
