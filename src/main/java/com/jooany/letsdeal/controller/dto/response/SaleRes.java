package com.jooany.letsdeal.controller.dto.response;

import java.sql.Timestamp;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SaleRes {
	private Long id;
	private String imageUrl;
	private String title;
	private Integer sellerPrice;
	private SaleStatus saleStatus;
	private Integer maxBuyerPrice;
	private Boolean isSeller;
	private Timestamp registeredAt;
	private Timestamp updateAt;

	@QueryProjection
	public SaleRes(Long id, String imageUrl, String title, Integer sellerPrice, SaleStatus saleStatus,
		Integer maxBuyerPrice, Boolean isSeller, Timestamp registeredAt, Timestamp updateAt) {
		this.id = id;
		this.imageUrl = imageUrl;
		this.title = title;
		this.sellerPrice = sellerPrice;
		this.saleStatus = saleStatus;
		this.maxBuyerPrice = maxBuyerPrice;
		this.isSeller = isSeller;
		this.registeredAt = registeredAt;
		this.updateAt = updateAt;
	}

	//    public static SaleRes from(Sale sale, Integer maxBuyerPrice){
	//        return new SaleRes(
	//                sale.getId(),
	//                sale.getImages().get(0).getImageUrl(),
	//                sale.getTitle(),
	//                sale.getSellerPrice(),
	//                sale.getSaleStatus(),
	//                maxBuyerPrice,
	//                sale
	//                sale.getRegisteredAt(),
	//                sale.getUpdateAt()
	//        );
	//    }
}
