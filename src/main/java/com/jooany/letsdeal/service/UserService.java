package com.jooany.letsdeal.service;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.cache.RefreshTokenCacheRepository;
import com.jooany.letsdeal.repository.cache.UserCacheRepository;
import com.jooany.letsdeal.repository.UserRepository;
import com.jooany.letsdeal.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;
    private final RefreshTokenCacheRepository refreshTokenCacheRepository;
    private final JwtTokenConfig jwtTokenConfig;

    public UserDto loadUserByUserName(String userName){
        return userCacheRepository.getUserDto(userName).orElseGet(() ->
                userRepository.findByUserName(userName).map(UserDto::from).orElseThrow(() ->
                        new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 는(은) 존재하지 않는 사용자입니다.", userName)))
        );
    }

    @Transactional
    public UserDto join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new LetsDealAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("\'%s\' 는 이미 사용 중입니다.", userName));
        });

        User user = userRepository.save(User.of(userName, encoder.encode(password)));
        return UserDto.from(user);
    }

    @Transactional
    public AuthTokens login(String userName, String password) {
        // 사용자 존재 확인
        UserDto userDto = loadUserByUserName(userName);

        // 비밀번호 일치 체크
        if(!encoder.matches(password,userDto.getPassword())){
            throw new LetsDealAppException(ErrorCode.INVALID_PASSWORD);
        }

        // AccessToken과 RefreshToken 생성
        AuthTokens authTokens = generateTokens(userName);
        // userDto, refreshToken 캐싱
        userCacheRepository.setUser(userDto);

        return authTokens;
    }

    @Transactional
    public AuthTokens generateTokens(String userName) {
        String newAccessToken = JwtTokenUtils.generateToken(userName, jwtTokenConfig.getAccessToken().getSecretKey(), jwtTokenConfig.getAccessToken().getExpiredTimeMs());
        String newRefreshToken = JwtTokenUtils.generateToken(userName, jwtTokenConfig.getRefreshToken().getSecretKey(), jwtTokenConfig.getRefreshToken().getExpiredTimeMs());

        AuthTokens authTokens = new AuthTokens(newAccessToken, newRefreshToken );
        refreshTokenCacheRepository.setRefreshToken(userName, newRefreshToken);

        return authTokens;
    }

    @Transactional
    public void delete(String userName) {
        userRepository.deleteByUserName(userName);
        userCacheRepository.deleteUser(userName);
        refreshTokenCacheRepository.deleteUser(userName);
    }


}
