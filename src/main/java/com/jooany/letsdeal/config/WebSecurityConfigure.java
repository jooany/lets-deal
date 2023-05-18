package com.jooany.letsdeal.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigure {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(HttpMethod.POST,"/api/*/users/join", "/api/*/users/login");
    }

//    private final UserService userService;
//
//    @Value("${jwt.secret-key}")
//    private String key;
//
//    // 정규화패턴에 적용되는 url 요청만 필터 적용
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().regexMatchers("^(?!/api/).*")
//                .antMatchers(HttpMethod.POST,"/api/*/users/join", "/api/*/users/login");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()// CSRF 공격 방지 기능을 비활성화
//                .authorizeRequests() // 인증 및 권한 검사
//                .antMatchers("/api/**").authenticated()
//                .and()
//                .sessionManagement()// 세션 관리 설정
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용X, RESTful API에서 보안을 강화하기 위해 세션을 사용하지 않는 Stateless한 방식 사용
//                .and()
//                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
//                // 필터에서 에러가 났을 때, 원하는 규정에 맞게 보여주기 위해서는 exceptionHandling 하고, EntryPoint를 반환해야 함.
//                .exceptionHandling()
//                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
//
//
//    }
}
