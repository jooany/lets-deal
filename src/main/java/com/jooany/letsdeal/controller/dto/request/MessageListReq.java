package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageListReq {
	@NotNull(message = "NULL일 수 없습니다.")
	private Long opponentId;
}
