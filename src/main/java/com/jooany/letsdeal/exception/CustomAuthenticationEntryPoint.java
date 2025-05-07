package com.jooany.letsdeal.exception;

import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.util.JsonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final JsonUtils jsonUtils;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
        response.getWriter()
                .write(
                        jsonUtils.toJson(
                                Response.error(ErrorCode.INVALID_TOKEN.name(), ErrorCode.INVALID_TOKEN.getMessage())
                        )
                );
    }
}