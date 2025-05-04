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
		http.csrf().disable().authorizeHttpRequests()
			.requestMatchers("/api/**").authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(
				new JwtTokenFilter(
					jwtTokenConfig.getAccessToken().getSecretKey(),
					jwtTokenConfig.getRefreshToken().getSecretKey(),
					userService,
					refreshTokenRepository),
				UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint());

		return http.build();
	}
}
