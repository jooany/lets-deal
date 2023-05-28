package com.jooany.letsdeal.config;

import com.jooany.letsdeal.config.filter.JwtTokenFilter;
import com.jooany.letsdeal.exception.CustomAuthenticationEntryPoint;
import com.jooany.letsdeal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {
    private final JwtTokenConfig jwtTokenConfig;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(HttpMethod.POST,"/api/*/users/join", "/api/*/users/login");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests() // 인증 및 권한 검사
                .requestMatchers("/api/**").authenticated()
                .and()
                .sessionManagement()// 세션 관리 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용X, RESTful API에서 보안을 강화하기 위해 세션을 사용하지 않는 Stateless한 방식 사용
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenConfig.getAccessToken().getSecretKey(), jwtTokenConfig.getRefreshToken().getSecretKey(), userService), UsernamePasswordAuthenticationFilter.class)
                // 필터에서 에러가 났을 때, 원하는 규정에 맞게 보여주기 위해서는 exceptionHandling 하고, EntryPoint를 반환해야 함.
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

            return http.build();
    }
}
