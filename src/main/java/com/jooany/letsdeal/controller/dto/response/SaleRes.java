package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.ImageDto;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class SaleRes {
    private Long id;
    private Long userId;
    private String userName;
    private Long categoryId;
    private String categoryName;
    private List<ImageDto> images;
    private Integer maxBuyerPrice;
    private String title;
    private String contents;
    private Integer sellerPrice;
    private SaleStatus saleStatus;
    private Timestamp registeredAt;
    private Timestamp updateAt;

    @QueryProjection
    public SaleRes(Long id, Long userId, String userName, Long categoryId, String categoryName, Integer maxBuyerPrice, String title, String contents, Integer sellerPrice, SaleStatus saleStatus, Timestamp registeredAt, Timestamp updateAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.maxBuyerPrice = maxBuyerPrice;
        this.title = title;
        this.contents = contents;
        this.sellerPrice = sellerPrice;
        this.saleStatus = saleStatus;
        this.registeredAt = registeredAt;
        this.updateAt = updateAt;
    }

//    public static SaleRes from(Sale sale, Image images, Integer maxBuyerPrice)
}
