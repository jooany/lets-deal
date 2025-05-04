package com.jooany.letsdeal.config.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.service.UserService;
import com.jooany.letsdeal.util.JwtTokenUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	private static final String AUTH_HEADER_PREFIX = "Bearer ";
	private static final String ACCESS_TOKEN_EMPTY = "Access token is empty";
	private static final String ERROR_GETTING_HEADER = "Error occurs while getting header. Header is null or invalid";
	private static final String ERROR_VALIDATING = "Error occurs while validating. {}";
	private final String accessTokenSecretKey;
	private final String refreshTokenSecretKey;
	private final UserService userService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {

		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || !header.startsWith(AUTH_HEADER_PREFIX)) {
			log.error(ERROR_GETTING_HEADER);
			filterChain.doFilter(request, response);
			return;
		}

		String[] s = header.split(" ");
		final String accessToken = s[1].trim();
		if ("".equals(accessToken)) {
			log.error(ACCESS_TOKEN_EMPTY);
			filterChain.doFilter(request, response);
			return;
		}

		if (!request.getRequestURI().equals("/api/v1/users/tokens")) {
			try {
				if (JwtTokenUtils.isExpired(accessToken, accessTokenSecretKey)) {
					log.error("Key is expired or Access token is expired");
					return;
				}

				String userName = JwtTokenUtils.getUserName(accessToken, accessTokenSecretKey);

				UserDto userDto = userService.loadUserByUserName(userName);
				userDto.setNullPassword();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDto, null, userDto.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);

			} catch (RuntimeException e) {
				log.error(ERROR_VALIDATING, e);
				filterChain.doFilter(request, response);
				return;
			}
		} else {
			String userName = "";
			try {
				String refreshToken = s[3].trim();
				userName = JwtTokenUtils.getUserName(refreshToken, refreshTokenSecretKey);
				if (JwtTokenUtils.isExpired(refreshToken, refreshTokenSecretKey)
					|| !refreshToken.equals(refreshTokenRepository.getRefreshToken(userName).orElse(""))) {
					log.error("Key is expired or Refresh token is expired");
					return;
				}

				UserDto userDto = userService.loadUserByUserName(userName);
				userDto.setNullPassword();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDto, null, userDto.getAuthorities());

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authToken);

			} catch (RuntimeException e) {
				log.error(ERROR_VALIDATING, e);
				filterChain.doFilter(request, response);
				return;
			}

			request.setAttribute("userName", userName);
		}

		filterChain.doFilter(request, response);
	}

}
