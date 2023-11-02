package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginReq {
	@NotBlank(message = "비어있을 수 없습니다.")
	private String userName;

	@NotBlank(message = "비어있을 수 없습니다.")
	private String password;
}
