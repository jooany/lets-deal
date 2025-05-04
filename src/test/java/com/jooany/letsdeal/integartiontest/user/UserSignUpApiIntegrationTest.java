package com.jooany.letsdeal.integartiontest.user;

import static com.jooany.letsdeal.fixture.dto.DtoFixture.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.integartiontest.AbstractIntegrationTest;

public class UserSignUpApiIntegrationTest extends AbstractIntegrationTest {

	@BeforeEach
	void setUp() {
		UserJoinReq userJoinReq = createUserJoinReq();
	}

	@Test
	void 회원가입_성공() {

	}

	@Test
	@DisplayName("닉네임 중복으로 회원가입에 실패한다.")
	void 닉네임_중복으로_실패() {

	}
}
