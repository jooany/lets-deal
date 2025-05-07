package com.jooany.letsdeal.config.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.service.UserService;
import com.jooany.letsdeal.util.JwtTokenUtils;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
	private static final String AUTH_HEADER_PREFIX = "Bearer ";
	private static final String REFRESH_HEADER = "X-Refresh-Token";
	private JwtTokenConfig jwtTokenConfig;
	private UserService userService;
	private RefreshTokenRepository refreshTokenRepository;

	public JwtTokenFilter(
		JwtTokenConfig jwtTokenConfig,
		UserService userService,
		RefreshTokenRepository refreshTokenRepository
	) {
		this.jwtTokenConfig = jwtTokenConfig;
		this.userService = userService;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		final String accessToken = extractToken(authHeader);
		if (accessToken.isBlank()) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			if (isRefreshRequest(request)) {
				String refreshToken = request.getHeader(REFRESH_HEADER);
				handleRefreshToken(request, refreshToken, jwtTokenConfig.getRefreshTokenSecretKey());
			} else {
				handleAccessToken(request, accessToken, jwtTokenConfig.getAccessTokenSecretKey());
			}
		} catch (JwtException | IllegalArgumentException e) {
			log.warn("JWT 검증 실패: {}", e.getMessage());
			filterChain.doFilter(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void handleAccessToken(HttpServletRequest request, String token, String accessTokenSecretKey) {
		if (JwtTokenUtils.isExpired(token, accessTokenSecretKey)) {
			throw new JwtException("Access Token 만료");
		}

		String username = JwtTokenUtils.getUserName(token, accessTokenSecretKey);
		authenticateUser(request, username);
	}

	private void handleRefreshToken(HttpServletRequest request, String refreshToken, String refreshTokenSecretKey) {
		if (refreshToken == null || refreshToken.isBlank()) {
			throw new JwtException("Refresh Token 없음");
		}

		if (JwtTokenUtils.isExpired(refreshToken, refreshTokenSecretKey)) {
			throw new JwtException("Refresh Token 만료");
		}

		String username = JwtTokenUtils.getUserName(refreshToken, refreshTokenSecretKey);
		String storedToken = refreshTokenRepository.getRefreshToken(username).orElse("");

		if (!refreshToken.equals(storedToken)) {
			throw new JwtException("Refresh Token 불일치");
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
}