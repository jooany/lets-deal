package com.jooany.letsdeal.exception;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.jooany.letsdeal.controller.dto.response.Response;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
		response.getWriter()
			.write(Response.error(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage()).toStream());
	}
}