package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.model.enumeration.SaleStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageGroupRes {
	private Long id;
	private Long saleId;
	private String title;
	private String thumbnailImageUrl;
	private SaleStatus saleStatus;
	private Boolean wasSaleDeleted;
	private Long opponentId;
	private String opponentNickname;
	private String lastMessageContent;
	private LocalDateTime lastMessageRegisteredAt;
	private Integer unreadMessageCount;
	private LocalDateTime registeredAt;
}
