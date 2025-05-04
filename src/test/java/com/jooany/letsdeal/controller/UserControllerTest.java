package com.jooany.letsdeal.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.controller.dto.request.UserLoginReq;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;
import com.jooany.letsdeal.service.UserService;

@AutoConfigureMockMvc
@ActiveProfiles("local")
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	void 회원가입_성공_회원정보저장() throws Exception {
		String userName = "userName";
		String password = "password!Pw123";
		String nickname = "nickname";

		given(userService.join(userName, password, nickname)).willReturn(mock(UserDto.class));

		mockMvc.perform(post("/api/v1/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserJoinReq(userName, password, nickname)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void 회원가입_실패_사용자ID중복() throws Exception {
		String userName = "userName";
		String password = "password!Pw123";
		String nickname = "nickname";

		given(userService.join(userName, password, nickname)).willThrow(
			new LetsDealAppException(ErrorCode.DUPLICATED_USER_NAME, "")
		);

		mockMvc.perform(post("/api/v1/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserJoinReq(userName, password, nickname)))
			).andDo(print())
			.andExpect(status().isConflict());
	}

	@Test
	void 로그인_성공() throws Exception {
		String userName = "userName";
		String password = "password";

		given(userService.login(userName, password)).willReturn(mock(AuthTokens.class));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginReq(userName, password)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void 로그인_실패_존재하지않는사용자() throws Exception {
		String userName = "userName";
		String password = "password";

		given(userService.login(userName, password)).willThrow(new LetsDealAppException(ErrorCode.USER_NOT_FOUND));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginReq(userName, password)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	void 로그인_실패_유효하지않은패스워드() throws Exception {
		String userName = "userName";
		String password = "password";

		given(userService.login(userName, password)).willThrow(new LetsDealAppException(ErrorCode.INVALID_PASSWORD));

		mockMvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginReq(userName, password)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 토큰재발급_성공_ACCESS토큰_및_REFRESH토큰_재발급() throws Exception {
		given(userService.generateTokens(any())).willReturn(new AuthTokens("", ""));

		mockMvc.perform(post("/api/v1/users/tokens")
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void 회원탈퇴_성공() throws Exception {
		mockMvc.perform(delete("/api/v1/users")
			).andDo(print())
			.andExpect(status().isOk());
	}

}
