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

import jakarta.persistence.EntityManager;
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
	private final EntityManager entityManager;

	public UserDto loadUserByUserName(String userName) {
		return userCacheRepository.getUserDto(userName).orElseGet(() ->
			userRepository.findByUserName(userName).map(UserDto::from).orElseThrow(() ->
				new LetsDealAppException(ErrorCode.USER_NOT_FOUND,
					String.format("%s 는(은) 존재하지 않는 사용자입니다.", userName)))
		);
	}

	@Transactional
	public UserDto join(String userName, String password, String nickname) {
		validateDuplicatedUserName(userName);

		checkDuplicateNickname(nickname);

		User user = userRepository.save(
			User.builder(
				userName,
				encoder.encode(password),
				nickname
			).build()
		);
		return UserDto.from(user);
	}

	private void validateDuplicatedUserName(String userName) {
		userRepository.findByUserName(userName)
			.ifPresent(it -> {
				throw new LetsDealAppException(
					ErrorCode.DUPLICATED_USER_NAME,
					String.format("\'%s\' 는 이미 사용 중입니다.", userName)
				);
			});
	}

	@Transactional
	public AuthTokens login(String userName, String password) {
		UserDto userDto = loadUserByUserName(userName);

		if (!encoder.matches(password, userDto.getPassword())) {
			throw new LetsDealAppException(ErrorCode.INVALID_PASSWORD);
		}

		AuthTokens authTokens = generateTokens(userName);

		userCacheRepository.setUser(userDto);

		return authTokens;
	}

	@Transactional
	public AuthTokens generateTokens(String userName) {
		String newAccessToken = jwtTokenUtils.generateToken(
			userName,
			jwtTokenConfig.getAccessTokenSecretKey(),
			jwtTokenConfig.getAccessTokenExpiredTimeMs()
		);
		String newRefreshToken = jwtTokenUtils.generateToken(
			userName,
			jwtTokenConfig.getRefreshTokenSecretKey(),
			jwtTokenConfig.getRefreshTokenExpiredTimeMs()
		);

		refreshTokenRepository.setRefreshToken(
			userName,
			newRefreshToken,
			jwtTokenConfig.getRefreshTokenExpiredTimeMs()
		);

		AuthTokens authTokens = new AuthTokens(newAccessToken, newRefreshToken);
		return authTokens;
	}

	@Transactional
	public void delete(String userName) {
		User user = getUserOrException(userName);
		userRepository.delete(user);
		userCacheRepository.deleteUser(userName);
		refreshTokenRepository.deleteUser(userName);

		messageService.deleteWithdrawnUserMessages(user.getId());
	}

	@Transactional
	public void updatePw(String beforePw, String afterPw, String userName) {
		User user = getUserOrException(userName);

		if (!encoder.matches(beforePw, user.getPassword())) {
			throw new LetsDealAppException(ErrorCode.INVALID_PREVIOUS_PASSWORD);
		}

		user.updatePw(encoder.encode(afterPw));
		entityManager.flush();

		userCacheRepository.setUser(UserDto.from(user));
	}

	@Transactional
	public void updateNick(String nickname, String userName) {
		User user = getUserOrException(userName);

		checkDuplicateNickname(nickname);

		user.updateNick(nickname);
		entityManager.flush();

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
