package com.jooany.letsdeal.controller.dto.request;

import com.jooany.letsdeal.model.enumeration.SaleStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {
	private SaleStatus saleStatus;
	private String keyword;
	private Long categoryId;
	private String targetedUserName;
	private String currentUserName;
	private Boolean isCurrentUserSale;
	private Boolean isCurrentUserOfferedProposal;

}
