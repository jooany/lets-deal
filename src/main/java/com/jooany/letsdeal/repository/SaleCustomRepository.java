package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.SaleDto;
import com.jooany.letsdeal.controller.dto.SaleWithMaxBuyerPriceDto;

import java.util.List;

public interface SaleCustomRepository {
    List<SaleWithMaxBuyerPriceDto> findSaleWithMaxBuyerPrice(SaleDto saleDto, Integer maxBuyerPrice);

}
