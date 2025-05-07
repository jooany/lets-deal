package com.jooany.letsdeal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jooany.letsdeal.config.filter.JwtTokenFilter;
import com.jooany.letsdeal.exception.CustomAuthenticationEntryPoint;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.service.UserService;
import com.jooany.letsdeal.util.JsonUtils;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {
	private final JsonUtils jsonUtils;

	@Bean
	public JwtTokenFilter jwtTokenFilter(
		JwtTokenConfig jwtTokenConfig,
		UserService userService,
		RefreshTokenRepository refreshTokenRepository
	) {
		return new JwtTokenFilter(jwtTokenConfig, userService, refreshTokenRepository);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.POST, "/api/v1/users/join", "/api/v1/users/login").permitAll()
			.requestMatchers("/api/**").authenticated()
			.and()
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint(jsonUtils));

		return http.build();
	}
}
