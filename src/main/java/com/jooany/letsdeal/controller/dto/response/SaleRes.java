package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.model.enumeration.SaleStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
	private LocalDateTime registeredAt;
	private LocalDateTime updateAt;

	@QueryProjection
	public SaleRes(Long id, String imageUrl, String title, Integer sellerPrice, SaleStatus saleStatus,
		Integer maxBuyerPrice, Boolean isSeller, LocalDateTime registeredAt, LocalDateTime updateAt) {
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
}
