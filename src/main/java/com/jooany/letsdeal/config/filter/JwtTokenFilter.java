package com.jooany.letsdeal.config.filter;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.service.UserService;
import com.jooany.letsdeal.util.JsonUtils;
import com.jooany.letsdeal.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private static final String REFRESH_HEADER = "X-Refresh-Token";
    private final JwtTokenConfig jwtTokenConfig;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JsonUtils jsonUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (jwtTokenConfig.getExcludedPaths().contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            log.warn("Authorization 헤더가 없거나 형식이 올바르지 않습니다.");
            sendError(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        final String accessToken = extractToken(authHeader);
        if (accessToken.isBlank()) {
            log.warn("Access token이 비어 있습니다.");
            sendError(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        try {
            if (isRefreshRequest(request)) {
                String refreshToken = request.getHeader(REFRESH_HEADER);
                handleRefreshToken(request, refreshToken, jwtTokenConfig.getRefreshTokenSecretKey());
            } else {
                handleAccessToken(request, accessToken, jwtTokenConfig.getAccessTokenSecretKey());
            }
        } catch (RuntimeException e) {
            log.error("JWT 토큰 검증 중 예외 발생", e);
            sendError(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleAccessToken(HttpServletRequest request, String token, String accessTokenSecretKey) {
        if (JwtTokenUtils.isExpired(token, accessTokenSecretKey)) {
            throw new RuntimeException("Access Token 만료");
        }

        String username = JwtTokenUtils.getUserName(token, accessTokenSecretKey);
        authenticateUser(request, username);
    }

    private void handleRefreshToken(HttpServletRequest request, String refreshToken, String refreshTokenSecretKey) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh Token 없음");
        }

        String username = JwtTokenUtils.getUserName(refreshToken, refreshTokenSecretKey);

        if (JwtTokenUtils.isExpired(refreshToken, refreshTokenSecretKey)) {
            throw new RuntimeException("Refresh Token 만료");
        }

        String storedToken = refreshTokenRepository.getRefreshToken(username).orElse("");
        if (!refreshToken.equals(storedToken)) {
            throw new RuntimeException("Refresh Token 불일치");
        }

        authenticateUser(request, username);
        request.setAttribute("userName", username);
    }

    private void authenticateUser(HttpServletRequest request, String username) {
        UserDto userDto = userService.loadUserByUserName(username);
        userDto.setNullPassword();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDto, null, userDto.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean isRefreshRequest(HttpServletRequest request) {
        return "/api/v1/users/tokens".equals(request.getRequestURI());
    }

    private String extractToken(String header) {
        return header.substring(AUTH_HEADER_PREFIX.length()).trim();
    }

    private void sendError(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(errorCode.getStatus().value());

        String json = jsonUtils.toJson(Response.error(errorCode.name(), errorCode.getMessage()));
        response.getWriter().write(json);
    }
}


