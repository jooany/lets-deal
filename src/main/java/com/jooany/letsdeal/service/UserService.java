package com.jooany.letsdeal.service;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.UserEntity;
import com.jooany.letsdeal.repository.RefreshTokenCacheRepository;
import com.jooany.letsdeal.repository.UserCacheRepository;
import com.jooany.letsdeal.repository.UserEntityRepository;
import com.jooany.letsdeal.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository userCacheRepository;
    private final RefreshTokenCacheRepository refreshTokenCacheRepository;

    @Value("${jwt.access-token.secret-key}")
    private String accessTokenSecretKey;

    @Value("${jwt.refresh-token.secret-key}")
    private String refreshTokenSecretKey;

    @Value("${jwt.access-token.expired-time-ms}")
    private Long accessTokenExpiredTimeMs;

    @Value("${jwt.refresh-token.expired-time-ms}")
    private Long refreshTokenExpiredTimeMs;
    public UserDto loadUserByUserName(String userName){
        return userCacheRepository.getUserDto(userName).orElseGet(() ->
                userEntityRepository.findByUserName(userName).map(UserDto::fromEntity).orElseThrow(() ->
                        new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 는(은) 존재하지 않는 사용자입니다.", userName)))
        );
    }

    @Transactional
    public UserDto join(String userName, String password) {
        userEntityRepository.findByUserName(userName).ifPresent(it -> {
            throw new LetsDealAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("\'%s\' 는 이미 사용 중입니다.", userName));
        });

        UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return UserDto.fromEntity(userEntity);
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
        String accessToken = JwtTokenUtils.generateToken(userDto.getId(), accessTokenSecretKey, accessTokenExpiredTimeMs);
        String refreshToken = JwtTokenUtils.generateToken(userDto.getId(), refreshTokenSecretKey, refreshTokenExpiredTimeMs);

        // userDto, refreshToken 캐싱
        userCacheRepository.setUser(userDto);
        refreshTokenCacheRepository.setRefreshToken(userDto.getId(), refreshToken);

        return new AuthTokens(accessToken, refreshToken);
    }

}
