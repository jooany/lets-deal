package com.jooany.letsdeal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProposalMessage {
	private Long productId;
	private String username;
	private Integer buyerPrice;
}
