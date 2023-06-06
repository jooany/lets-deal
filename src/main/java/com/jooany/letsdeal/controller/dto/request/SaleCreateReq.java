package com.jooany.letsdeal.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleCreateReq {
    private Long categoryId;
    private String title;
    private String contents;
    private Integer sellerPrice;
}
