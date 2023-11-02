package com.jooany.letsdeal.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import com.jooany.letsdeal.config.JwtTokenConfig;
import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.fixture.dto.DtoFixture;
import com.jooany.letsdeal.fixture.entity.EntityFixture;
import com.jooany.letsdeal.model.entity.User;
import com.jooany.letsdeal.repository.UserRepository;
import com.jooany.letsdeal.repository.redis.RefreshTokenRepository;
import com.jooany.letsdeal.repository.redis.UserCacheRepository;
import com.jooany.letsdeal.util.JwtTokenUtils;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("local")
public class UserServiceTest {
	@InjectMocks
	private UserService userService;

	@Mock
	private MessageService messageService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BCryptPasswordEncoder encoder;

	@Mock
	private JwtTokenConfig jwtTokenConfig;

	@Mock
	private JwtTokenUtils jwtTokenUtils;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Mock
	private UserCacheRepository userCacheRepository;

	@DisplayName("신규회원가입 - 성공")
	@Test
	void join() {
		String userName = "testUser";
		String password = "testPassword";
		String encodedPassword = "encodedPassword";
		String nickname = "nickname";
		User user = EntityFixture.createUser(userName, encodedPassword, nickname);
		given(userRepository.findByUserName(userName)).willReturn(Optional.empty());
		given(encoder.encode(password)).willReturn(encodedPassword);
		given(userRepository.save(any(User.class))).willReturn(user);

		UserDto result = userService.join(userName, password, nickname);

		verify(userRepository, times(1)).findByUserName(userName);
		verify(userRepository, times(1)).findByNickname(nickname);
		verify(encoder, times(1)).encode(password);
		verify(userRepository, times(1)).save(any(User.class));
		assertThat(result).isNotNull();
		assertThat(result.getUsername()).isEqualTo(userName);
		assertThat(result.getPassword()).isEqualTo(encodedPassword);
	}

	@DisplayName("신규회원가입_아이디가 중복인 경우 - 실패")
	@Test
	void join_DuplicateUserName() {
		String userName = "testUser";
		String password = "testPassword";
		String encodedPassword = "encodedPassword";
		String nickname = "nickname";
		User user = EntityFixture.createUser(userName, encodedPassword, nickname);

		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		LetsDealAppException e = assertThrows(LetsDealAppException.class,
			() -> userService.join(userName, password, nickname));
		assertEquals(ErrorCode.DUPLICATED_USER_NAME, e.getErrorCode());
		verify(userRepository, times(1)).findByUserName(user.getUserName());
	}

	@DisplayName("신규회원가입_닉네임이 중복인 경우 - 실패")
	@Test
	void join_DuplicateNickname() {
		String userName = "testUser";
		String password = "testPassword";
		String encodedPassword = "encodedPassword";
		String nickname = "nickname";
		User user = EntityFixture.createUser(userName, encodedPassword, nickname);

		given(userRepository.findByNickname(nickname)).willReturn(Optional.of(user));

		LetsDealAppException e = assertThrows(LetsDealAppException.class,
			() -> userService.join(userName, password, nickname));
		assertEquals(ErrorCode.DUPLICATED_NICKNAME, e.getErrorCode());
		verify(userRepository, times(1)).findByUserName(user.getUserName());
	}

	@DisplayName("로그인, 사용자 인증 후 인증 토큰 반환 및 사용자 정보 캐싱 - 성공")
	@Test
	void login() {
		//Given
		String userName = "testUser";
		String fakeAccessToken = "fakeAccessToken";
		String fakeRefreshToken = "fakeRefreshToken";

		// JwtTokenConfig에 필요한 비밀키 설정
		JwtTokenConfig.AccessToken accessToken = new JwtTokenConfig.AccessToken();
		accessToken.setSecretKey("access_secret_key123123123123123123123");
		accessToken.setExpiredTimeMs(3600000L);
		JwtTokenConfig.RefreshToken refreshToken = new JwtTokenConfig.RefreshToken();
		refreshToken.setSecretKey("refresh_secret_key12311123123123123123123");
		refreshToken.setExpiredTimeMs(86400000L);

		given(jwtTokenConfig.getAccessToken()).willReturn(accessToken);
		given(jwtTokenConfig.getRefreshToken()).willReturn(refreshToken);

		//가짜 토큰 설정
		given(jwtTokenUtils.generateToken(userName, "access_secret_key123123123123123123123", 3600000L))
			.willReturn(fakeAccessToken);
		given(jwtTokenUtils.generateToken(userName, "refresh_secret_key12311123123123123123123", 86400000L))
			.willReturn(fakeRefreshToken);

		// When
		AuthTokens authTokens = userService.generateTokens(userName);

		// Then
		assertNotNull(authTokens);
		assertEquals(fakeAccessToken, authTokens.getAccessToken());
		assertEquals(fakeRefreshToken, authTokens.getRefreshToken());
		verify(jwtTokenUtils, times(2)).generateToken(eq(userName), any(String.class), any(Long.class));
	}

	@DisplayName("로그인_존재하지 않는 사용자인 경우 - 실패")
	@Test
	void login_userNotFound() {
		String userName = "testUser";
		String password = "password";

		given(userCacheRepository.getUserDto(userName)).willReturn(Optional.empty());
		given(userRepository.findByUserName(userName)).willReturn(Optional.empty());

		LetsDealAppException e = Assertions.assertThrows(LetsDealAppException.class,
			() -> userService.login(userName, password));
		assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
		verify(userCacheRepository, times(1)).getUserDto(userName);
		verify(userRepository, times(1)).findByUserName(userName);

	}

	@DisplayName("로그인_일치하지 않는 비밀번호인 경우 - 실패")
	@Test
	void login_invalidPassword() {
		String userName = "testUser";
		String password = "invalidPassword";

		UserDto userDto = DtoFixture.createUserDto();
		given(userCacheRepository.getUserDto(userName)).willReturn(Optional.of(userDto));
		given(encoder.matches(password, userDto.getPassword())).willReturn(false);

		LetsDealAppException e = assertThrows(LetsDealAppException.class, () -> userService.login(userName, password));
		assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
		verify(userCacheRepository, times(1)).getUserDto(userName);
		verify(encoder, times(1)).matches(password, userDto.getPassword());
	}

	@DisplayName("회원 데이터 삭제 - 성공")
	@Test
	void delete() {
		String userName = "testUser";
		User user = EntityFixture.createUser();
		given(userRepository.findByUserName(userName)).willReturn(Optional.of(user));

		userService.delete(userName);

		verify(userRepository, times(1)).delete(eq(user));
		verify(userCacheRepository, times(1)).deleteUser(eq(userName));
		verify(refreshTokenRepository, times(1)).deleteUser(eq(userName));
	}

	@DisplayName("비밀번호 변경 - 성공")
	@Test
	public void updatePw() {
		User user = EntityFixture.createUser();

		// userRepository.findByUserName(userName)이 호출될 때 가짜 유저 객체를 반환하도록 설정
		given(userRepository.findByUserName("testUser")).willReturn(Optional.of(user));
		// encoder.matches()가 호출될 때 true를 반환하도록 설정 (기존 비밀번호가 일치한다고 가정)
		given(encoder.matches(anyString(), anyString())).willReturn(true);

		userService.updatePw("oldPassword", "newPassword", "testUser");

		verify(userCacheRepository).setUser(any(UserDto.class));
	}

	@DisplayName("비밀번호 변경_이전 비밀번호 일치하지 않는 경우 - 실패")
	@Test
	public void updatePw_invalidPreviousPw() {
		User user = EntityFixture.createUser();
		given(userRepository.findByUserName("testUser")).willReturn(Optional.of(user));
		given(encoder.matches(anyString(), anyString())).willReturn(false);

		LetsDealAppException e = assertThrows(LetsDealAppException.class,
			() -> userService.updatePw("oldPassword", "newPassword", "testUser"));
		assertEquals(ErrorCode.INVALID_PREVIOUS_PASSWORD, e.getErrorCode());
	}

	@DisplayName("닉네임 변경 - 성공")
	@Test
	public void updateNick() {
		User user = EntityFixture.createUser();

		// userRepository.findByUserName(userName)이 호출될 때 가짜 유저 객체를 반환하도록 설정
		given(userRepository.findByUserName("testUser")).willReturn(Optional.of(user));
		// userRepository.findByNickname(nickname)이 호출될 때 null을 반환하도록 설정 (중복되지 않는다고 가정)
		given(userRepository.findByNickname("newNickname")).willReturn(Optional.empty());

		userService.updateNick("newNickname", "testUser");

		verify(userCacheRepository).setUser(any(UserDto.class));
	}

	@DisplayName("닉네임 변경_닉네임이 중복인 경우 - 실패")
	@Test
	public void updateNick_duplicateNickname() {
		User user = EntityFixture.createUser();

		given(userRepository.findByUserName("testUser")).willReturn(Optional.of(user));
		given(userRepository.findByNickname("nickname")).willReturn(Optional.of(user));

		LetsDealAppException e = assertThrows(LetsDealAppException.class,
			() -> userService.updateNick("nickname", "testUser"));
		assertEquals(ErrorCode.DUPLICATED_NICKNAME, e.getErrorCode());
	}

}
