package com.jooany.letsdeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jooany.letsdeal.config.filter.JwtTokenFilter;
import com.jooany.letsdeal.exception.CustomAuthenticationEntryPoint;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {
	private final JwtTokenConfig jwtTokenConfig;
	private final UserService userService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers(HttpMethod.POST, "/api/*/users/join", "/api/*/users/login");
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeHttpRequests() // 인증 및 권한 검사
			.requestMatchers("/api/**").authenticated()
			.and()
			.sessionManagement()// 세션 관리 설정
			.sessionCreationPolicy(
				SessionCreationPolicy.STATELESS) // 세션 사용X, RESTful API에서 보안을 강화하기 위해 세션을 사용하지 않는 Stateless한 방식 사용
			.and()
			.addFilterBefore(new JwtTokenFilter(jwtTokenConfig.getAccessToken().getSecretKey(),
					jwtTokenConfig.getRefreshToken().getSecretKey(), userService, refreshTokenRepository),
				UsernamePasswordAuthenticationFilter.class)
			// SpringSecurity에서 인증 실패 시의 처리를 정의
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint());

		return http.build();
	}
}
