package com.jooany.letsdeal.controller.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageListRes {
	private Boolean isOpponentWithdrawn;
	List<MessageRes> messageList;
}
