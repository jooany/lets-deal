package com.jooany.letsdeal.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleWithMaxBuyerPriceDto {
    private SaleDto saleDto;
    private Integer maxBuyerPrice;
}
