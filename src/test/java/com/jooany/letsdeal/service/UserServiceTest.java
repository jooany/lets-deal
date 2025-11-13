package com.jooany.letsdeal.service;

import static com.jooany.letsdeal.TestUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.UserRepository;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.repository.redis.UserCacheRepository;
import com.jooany.letsdeal.util.JwtTokenUtils;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private MessageService messageService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtTokenConfig jwtTokenConfig;

	@Mock
	private JwtTokenUtils jwtTokenUtils;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private UserCacheRepository userCacheRepository;

	@Mock
	private EntityManager entityManager;

	/**
	 * [중요] BCryptPasswordEncoder는 Mocking하지 않습니다.
	 * 실제 객체를 생성하여, encode/matches 로직이 서비스의 트랜잭션과
	 * 올바르게 연동되는지 검증해야 합니다.
	 */

	private BCryptPasswordEncoder encoder;

	// --- Test Fixtures ---
	private String userName;
	private String password;
	private String encodedPassword;
	private String nickname;
	private User user;

	@BeforeEach
	void setUp() {
		encoder = new BCryptPasswordEncoder();

		// @InjectMocks 대신 수동 주입 (BCryptPasswordEncoder 실제 객체 주입을 위함)
		userService = new UserService(
			userRepository,
			encoder,
			userCacheRepository,
			refreshTokenRepository,
			jwtTokenConfig,
			jwtTokenUtils,
			messageService,
			entityManager
		);

		// 공통 픽스처 설정
		userName = "testUser";
		password = "testPassword123";
		encodedPassword = encoder.encode(password);
		nickname = "nickname";
		user = EntityFixture.createUser(userName, encodedPassword, nickname);
	}

	@DisplayName("신규회원가입 - 성공")
	@Test
	void join_success() {
		// given
		given(userRepository.findByUserName(userName)).willReturn(Optional.empty());
		given(userRepository.findByNickname(nickname)).willReturn(Optional.empty());
		given(userRepository.save(any(User.class))).willReturn(user);

		// when
		UserDto result = userService.join(userName, password, nickname);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUsername()).isEqualTo(userName);
		assertThat(result.getNickname()).isEqualTo(nickname);
		assertThat(result.getPassword()).isEqualTo(encodedPassword);

		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		then(userRepository).should().save(userCaptor.capture());
		User savedUser = userCaptor.getValue();

		// 비밀번호가 올바르게 인코딩되었는지 확인
		assertThat(encoder.matches(password, savedUser.getPassword())).isTrue();
		assertThat(savedUser.getPassword()).isNotEqualTo(password);
		assertThat(savedUser.getUserName()).isEqualTo(userName);
	}

	@DisplayName("신규회원가입_아이디가 중복인 경우 - 실패")
	@Test
	void join_fail_duplicateUserName() {
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		assertException(
			LetsDealAppException.class,
			() -> userService.join(userName, password, nickname),
			ErrorCode.DUPLICATED_USER_NAME
		);
		then(userRepository).should(never()).findByNickname(anyString());
		then(userRepository).should(never()).save(any(User.class));
	}

	@DisplayName("신규회원가입_닉네임이 중복인 경우 - 실패")
	@Test
	void join_fail_duplicateNickname() {
		given(userRepository.findByNickname(nickname)).willReturn(Optional.of(user));

		assertException(
			LetsDealAppException.class,
			() -> userService.join(userName, password, nickname),
			ErrorCode.DUPLICATED_NICKNAME
		);
		then(userRepository).should(never()).save(any(User.class));
	}

	@DisplayName("로그인 - 성공")
	@Test
	void login_success() {
		// given
		// 1. 캐시 미스 시나리오
		given(userCacheRepository.getUserDto(userName)).willReturn(Optional.empty());
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		// 2. 토큰 생성 Mocking
		String fakeAccessToken = "fakeAccessToken";
		String fakeRefreshToken = "fakeRefreshToken";
		long accessTokenExpiredTimeMs = 3600000L;
		long refreshTokenExpiredTimeMs = 86400000L;
		String fakeAccessTokenSecretKey = "access_secret_key123123123123123123123";
		String fakeRefreshTokenSecretKey = "refresh_secret_key12311123123123123123123";
		given(jwtTokenConfig.getAccessTokenSecretKey()).willReturn(fakeAccessTokenSecretKey);
		given(jwtTokenConfig.getRefreshTokenSecretKey()).willReturn(fakeRefreshTokenSecretKey);
		given(jwtTokenConfig.getAccessTokenExpiredTimeMs()).willReturn(accessTokenExpiredTimeMs);
		given(jwtTokenConfig.getRefreshTokenExpiredTimeMs()).willReturn(refreshTokenExpiredTimeMs);

		given(jwtTokenUtils.generateToken(any(), any(), anyLong()))
			.willReturn(fakeAccessToken)
			.willReturn(fakeRefreshToken);

		// when
		AuthTokens authTokens = userService.login(userName, password);

		// then
		// 1. 토큰 검증
		assertThat(authTokens).isNotNull();
		assertThat(authTokens.getAccessToken()).isEqualTo(fakeAccessToken);
		assertThat(authTokens.getRefreshToken()).isEqualTo(fakeRefreshToken);

		// 2. 유저 정보 캐시 저장
		ArgumentCaptor<UserDto> userDtoCaptor = ArgumentCaptor.forClass(UserDto.class);
		then(userCacheRepository).should().setUser(userDtoCaptor.capture());
		assertThat(userDtoCaptor.getValue().getUsername()).isEqualTo(userName);

		// 3. 리프레시 토큰 Redis 저장
		then(refreshTokenRepository).should().setRefreshToken(userName, fakeRefreshToken, refreshTokenExpiredTimeMs);
	}

	@DisplayName("로그인_존재하지 않는 사용자인 경우 - 실패")
	@Test
	void login_fail_userNotFound() {
		given(userCacheRepository.getUserDto(userName)).willReturn(Optional.empty());
		given(userRepository.findByUserName(userName)).willReturn(Optional.empty());

		assertException(
			LetsDealAppException.class,
			() -> userService.login(userName, password),
			ErrorCode.USER_NOT_FOUND
		);

		then(userCacheRepository).should(never()).setUser(any());
		then(refreshTokenRepository).should(never()).setRefreshToken(any(), any(), any());
	}

	@DisplayName("로그인_일치하지 않는 비밀번호인 경우 - 실패")
	@Test
	void login_fail_invalidPassword() {
		given(userCacheRepository.getUserDto(userName)).willReturn(Optional.empty());
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
		String wrongPassword = "invalidPassword";

		assertException(
			LetsDealAppException.class,
			() -> userService.login(userName, wrongPassword),
			ErrorCode.INVALID_PASSWORD
		);

		then(userCacheRepository).should(never()).setUser(any());
		then(refreshTokenRepository).should(never()).setRefreshToken(any(), any(), any());
	}

	@DisplayName("토큰 생성 로직 - 성공")
	@Test
	void generateTokens_success() {
		//given
		String fakeAccessToken = "fakeAccessToken";
		String fakeRefreshToken = "fakeRefreshToken";
		long refreshTokenExpiredTimeMs = 86400000L;
		long accessTokenExpiredTimeMs = 3600000L;

		given(jwtTokenConfig.getAccessTokenSecretKey()).willReturn("access_secret_key");
		given(jwtTokenConfig.getAccessTokenExpiredTimeMs()).willReturn(accessTokenExpiredTimeMs);
		given(jwtTokenConfig.getRefreshTokenSecretKey()).willReturn("refresh_secret_key");
		given(jwtTokenConfig.getRefreshTokenExpiredTimeMs()).willReturn(refreshTokenExpiredTimeMs);

		given(jwtTokenUtils.generateToken(userName, "access_secret_key", accessTokenExpiredTimeMs))
			.willReturn(fakeAccessToken);
		given(jwtTokenUtils.generateToken(userName, "refresh_secret_key", refreshTokenExpiredTimeMs))
			.willReturn(fakeRefreshToken);

		// when
		AuthTokens authTokens = userService.generateTokens(userName);

		// then
		assertThat(authTokens).isNotNull();
		assertThat(authTokens.getAccessToken()).isEqualTo(fakeAccessToken);
		assertThat(authTokens.getRefreshToken()).isEqualTo(fakeRefreshToken);

		// 토큰 저장 로직 호출 검증
		then(refreshTokenRepository).should().setRefreshToken(userName, fakeRefreshToken, refreshTokenExpiredTimeMs);
	}

	@DisplayName("회원 탈퇴 - 성공")
	@Test
	void delete_success() {
		String userName = "testUser";
		User user = EntityFixture.createUser();
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		userService.delete(userName);

		// 연관된 데이터 삭제(메시지) 검증
		then(messageService).should().deleteWithdrawnUserMessages(user.getId());

		// 캐시 및 토큰 삭제 검증
		then(userRepository).should().delete(eq(user));
		then(userCacheRepository).should().deleteUser(eq(userName));
		then(refreshTokenRepository).should().deleteUser(eq(userName));
	}

	@DisplayName("회원탈퇴_존재하지 않는 사용자 - 실패")
	@Test
	void delete_fail_userNotFound() {
		given(userRepository.findByUserName(userName)).willReturn(Optional.empty());

		assertException(
			LetsDealAppException.class,
			() -> userService.delete(userName),
			ErrorCode.USER_NOT_FOUND
		);
	}

	@DisplayName("비밀번호 변경 - 성공")
	@Test
	public void updatePw_success() {
		String newPassword = "newPassword456";
		given(userRepository.findByUserName("testUser")).willReturn(Optional.of(user));

		userService.updatePw(password, newPassword, userName);

		assertThat(encoder.matches(newPassword, user.getPassword())).isTrue();
		assertThat(encoder.matches(password, user.getPassword())).isFalse();

		then(entityManager).should().flush();
		then(userCacheRepository).should().setUser(any(UserDto.class));
	}

	@DisplayName("비밀번호 변경_이전 비밀번호 불일치 - 실패")
	@Test
	public void updatePw_fail_invalidPreviousPw() {
		String wrongOldPassword = "wrongOldPassword";
		String newPassword = "newPassword456";
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		assertException(
			LetsDealAppException.class,
			() -> userService.updatePw(wrongOldPassword, newPassword, userName),
			ErrorCode.INVALID_PREVIOUS_PASSWORD
		);

		then(entityManager).should(never()).flush();
		then(userCacheRepository).should(never()).setUser(any());
	}

	@DisplayName("닉네임 변경 - 성공")
	@Test
	public void updateNick_success() {
		String newNickname = "newNickname";
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
		given(userRepository.findByNickname(newNickname)).willReturn(Optional.empty());

		userService.updateNick(newNickname, userName);

		assertThat(user.getNickname()).isEqualTo(newNickname);

		then(entityManager).should().flush();
		then(userCacheRepository).should().setUser(any(UserDto.class));
	}

	@DisplayName("닉네임 변경_닉네임이 중복인 경우 - 실패")
	@Test
	public void updateNick_fail_duplicateNickname() {
		String newNickname = "newNickname";
		User existingUser = EntityFixture.createUser("anotherUser", "testPassword456", newNickname);

		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));
		given(userRepository.findByNickname(newNickname)).willReturn(Optional.of(existingUser));

		assertException(
			LetsDealAppException.class,
			() -> userService.updateNick(newNickname, userName),
			ErrorCode.DUPLICATED_NICKNAME
		);

		then(entityManager).should(never()).flush();
		then(userCacheRepository).should(never()).setUser(any());
	}

}
