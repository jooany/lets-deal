package com.jooany.letsdeal.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SaleSaveReq {
    private Long categoryId;
    private String title;
    private String contents;
    private Integer sellerPrice;
}
