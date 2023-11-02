package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinReq {
	@NotBlank(message = "비어있을 수 없습니다.")
	@Size(min = 4, max = 10, message = "4자 이상 10자 이하여야 합니다.")
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "영문자, 숫자만으로 구성되어야 합니다.")
	private String userName;

	@NotBlank(message = "비어있을 수 없습니다.")
	@Size(min = 4, max = 15, message = "4자 이상 15자 이하여야 합니다.")
	@Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "대문자,소문자,숫자,특수문자 각 1개 이상 포함되어야 합니다.")
	private String password;

	@NotBlank(message = "비어있을 수 없습니다.")
	@Size(min = 2, max = 10, message = "2자 이상 10자 이하여야 합니다.")
	@Pattern(regexp = "^[가-힣a-zA-Z0-9_]*[ㄱ-ㅎㅏ-ㅣ]*$", message = "한글, 영문자, 숫자, 특수문자[_] 만으로 구성되어야 합니다.")
	private String nickname;
}
