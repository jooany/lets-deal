package com.jooany.letsdeal.controller.dto.response;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MessageRes {
	private Long id;
	private String messageContent;
	private Boolean isSender;
	private Boolean wasReadBySender;
	private Timestamp registeredAt;
}
