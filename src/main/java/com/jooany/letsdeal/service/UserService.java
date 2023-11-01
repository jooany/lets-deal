package com.jooany.letsdeal.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.UserRepository;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.repository.redis.UserCacheRepository;
import com.jooany.letsdeal.util.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder encoder;
	private final UserCacheRepository userCacheRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenConfig jwtTokenConfig;

	private final JwtTokenUtils jwtTokenUtils;
	private final MessageService messageService;

	public UserDto loadUserByUserName(String userName) {
		return userCacheRepository.getUserDto(userName).orElseGet(() ->
			userRepository.findByUserName(userName).map(UserDto::from).orElseThrow(() ->
				new LetsDealAppException(ErrorCode.USER_NOT_FOUND,
					String.format("%s 는(은) 존재하지 않는 사용자입니다.", userName)))
		);
	}

	@Transactional
	public UserDto join(String userName, String password, String nickname) {

		// 아이디 중복 체크
		userRepository.findByUserName(userName).ifPresent(it -> {
			throw new LetsDealAppException(ErrorCode.DUPLICATED_USER_NAME,
				String.format("\'%s\' 는 이미 사용 중입니다.", userName));
		});

		// 닉네임 중복 체크
		checkDuplicateNickname(nickname);

		User user = userRepository.save(User.of(userName, encoder.encode(password), nickname));
		return UserDto.from(user);
	}

	@Transactional
	public AuthTokens login(String userName, String password) {
		// 사용자 존재 확인
		UserDto userDto = loadUserByUserName(userName);

		// 비밀번호 일치 체크
		if (!encoder.matches(password, userDto.getPassword())) {
			throw new LetsDealAppException(ErrorCode.INVALID_PASSWORD);
		}

		// 토큰 생성 및 refreshToken 저장
		AuthTokens authTokens = generateTokens(userName);

		// userDto 캐싱
		userCacheRepository.setUser(userDto);

		return authTokens;
	}

	@Transactional
	public AuthTokens generateTokens(String userName) {
		String newAccessToken = jwtTokenUtils.generateToken(userName, jwtTokenConfig.getAccessToken().getSecretKey(),
			jwtTokenConfig.getAccessToken().getExpiredTimeMs());
		String newRefreshToken = jwtTokenUtils.generateToken(userName, jwtTokenConfig.getRefreshToken().getSecretKey(),
			jwtTokenConfig.getRefreshToken().getExpiredTimeMs());

		refreshTokenRepository.setRefreshToken(userName, newRefreshToken,
			jwtTokenConfig.getRefreshToken().getExpiredTimeMs());

		AuthTokens authTokens = new AuthTokens(newAccessToken, newRefreshToken);
		return authTokens;
	}

	@Transactional
	public void delete(String userName) {
		User user = getUserOrException(userName);
		userRepository.delete(user);
		userCacheRepository.deleteUser(userName);
		refreshTokenRepository.deleteUser(userName);

		// 탈퇴하는 사용자의 메시지도 soft & hard Delete
		messageService.deleteWithdrawnUserMessages(user.getId());
	}

	@Transactional
	public void updatePw(String beforePw, String afterPw, String userName) {
		User user = getUserOrException(userName);

		// 기존 비밀번호 일치 체크
		if (!encoder.matches(beforePw, user.getPassword())) {
			throw new LetsDealAppException(ErrorCode.INVALID_PREVIOUS_PASSWORD);
		}

		user.updatePw(encoder.encode(afterPw));

		// 변경된 user 캐시 삭제 후 추가
		userCacheRepository.deleteUser(userName);
		userCacheRepository.setUser(UserDto.from(user));
	}

	@Transactional
	public void updateNick(String nickname, String userName) {
		User user = getUserOrException(userName);

		// 닉네임 중복체크
		checkDuplicateNickname(nickname);

		user.updateNick(nickname);

		// 변경된 user 캐시 삭제 후 추가
		userCacheRepository.deleteUser(userName);
		userCacheRepository.setUser(UserDto.from(user));
	}

	private User getUserOrException(String userName) {
		return userRepository.findByUserName(userName).orElseThrow(() ->
			new LetsDealAppException(ErrorCode.USER_NOT_FOUND, String.format("%s 사용자를 찾을 수 없습니다.", userName)));
	}

	private void checkDuplicateNickname(String nickname) {
		userRepository.findByNickname(nickname).ifPresent(it -> {
			throw new LetsDealAppException(ErrorCode.DUPLICATED_NICKNAME,
				String.format("\'%s\' 는 이미 사용 중입니다.", nickname));
		});
	}
}
