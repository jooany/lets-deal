package com.jooany.letsdeal.config.filter;

import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.repository.cache.RefreshTokenCacheRepository;
import com.jooany.letsdeal.service.UserService;
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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
* 모든 HTTP 요청마다 한번씩 실행되는 필터
* <공통>
* 토큰의 유효성을 체크하고, 요청 사용자의 식별 정보를 추출하여 전달한다.
*
* <토큰 재발급 요청이 아닌 경우>
* JWT 토큰에서 유저 이름을 추출하고, 해당 유저 이름을 사용하여 유저 정보를 가져와 인증 토큰을 생성한 다음, 인증된 토큰을 Security Context에 설정하는 역할
*
* 요청 헤더에 전달된 토큰을 검증 후, 해당 토큰에 들어있던 사용자 정보를 얻어 인증을 한다.
* 인증을 하며 가져온 사용자 객체를 Spring Security의 토큰 객체에 담고,
* SecurityContextHolder의 Context에 인증된 토큰 객체를 설정하면,
* Spring Security가 현재 인증된 유저를 추적하고 유저의 권한을 확인할 수 있게 한다.
*
* <토큰 재발급 요청인 경우>
* access token이 유효하지 않고, refresh token이 유효한지 검증
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final String accessTokenSecretKey;
    private final String refreshTokenSecretKey;
    private final UserService userService;
    private final RefreshTokenCacheRepository refreshTokenCacheRepository;
    private static final String AUTH_HEADER_PREFIX = "Bearer ";
   private static final String ACCESS_TOKEN_EMPTY = "Access token is empty";
   private static final String ERROR_GETTING_HEADER = "Error occurs while getting header. Header is null or invalid";
   private static final String ERROR_VALIDATING = "Error occurs while validating. {}";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더 유효성 검사
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(AUTH_HEADER_PREFIX)) {
            log.error(ERROR_GETTING_HEADER);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유효성 검사
        String[] s = header.split(" ");
        final String accessToken = s[1].trim();
        if("".equals(accessToken)){
            log.error(ACCESS_TOKEN_EMPTY);
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 재발급 요청이 아닐 시
        if(!request.getRequestURI().equals("/api/v1/users/tokens")) {
            try {
                // access token 유효성 체크
                if(JwtTokenUtils.isExpired(accessToken,accessTokenSecretKey)){
                    log.error("Key is expired or Access token is expired");
                    return;
                }

                // 토큰에서 사용자 정보 추출
                String userName = JwtTokenUtils.getUserName(accessToken, accessTokenSecretKey);

                // 사용자가 회원이면 객체 생성
                UserDto userDto = userService.loadUserByUserName(userName);

                // 회원 정보를 담은 인증 객체 생성
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDto, null, userDto.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 현재 실행 중인 스레드에 대한 인증 객체를 설정
                // 인증이 완료된 사용자의 Authentication 객체를 현재 실행 중인 스레드의 SecurityContext에 설정
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }catch (RuntimeException e){
                log.error(ERROR_VALIDATING, e);
                filterChain.doFilter(request, response);
                return;
            }
        }else { // 토큰 재발급 요청 시
            String userName = "";
            try {
                // refreshToken 유효성 체크
                String refreshToken = s[3].trim();
                userName = JwtTokenUtils.getUserName(refreshToken, refreshTokenSecretKey);
                if (JwtTokenUtils.isExpired(refreshToken, refreshTokenSecretKey)
                    || !refreshToken.equals(refreshTokenCacheRepository.getRefreshToken(userName).orElse(""))) {
                    log.error("Key is expired or Refresh token is expired");
                    return;
                }

                // 사용자 검증 및 인증 객체 생성
                UserDto userDto = userService.loadUserByUserName(userName);
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
