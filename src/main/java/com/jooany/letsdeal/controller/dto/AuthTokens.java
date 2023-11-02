package com.jooany.letsdeal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokens {
	private String accessToken;
	private String refreshToken;
}