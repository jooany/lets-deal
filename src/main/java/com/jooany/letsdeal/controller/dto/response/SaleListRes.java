package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.model.entity.Sale;
import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
public class SaleListRes {
    private Long id;
    private String imageUrl;
    private String title;
    private Integer sellerPrice;
    private SaleStatus saleStatus;
    private Integer maxBuyerPrice;
    private Timestamp registeredAt;
    private Timestamp updateAt;

    @QueryProjection
    public SaleListRes(Long id, String imageUrl, String title, Integer sellerPrice, SaleStatus saleStatus, Integer maxBuyerPrice, Timestamp registeredAt, Timestamp updateAt) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.sellerPrice = sellerPrice;
        this.saleStatus = saleStatus;
        this.maxBuyerPrice = maxBuyerPrice;
        this.registeredAt = registeredAt;
        this.updateAt = updateAt;
    }

    public static SaleListRes from(Sale sale, Integer maxBuyerPrice){
        return new SaleListRes(
                sale.getId(),
                sale.getImages().get(0).getImageUrl(),
                sale.getTitle(),
                sale.getSellerPrice(),
                sale.getSaleStatus(),
                maxBuyerPrice,
                sale.getRegisteredAt(),
                sale.getUpdateAt()
        );
    }
}
