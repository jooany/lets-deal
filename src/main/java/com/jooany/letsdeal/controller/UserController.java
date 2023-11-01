package com.jooany.letsdeal.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.controller.dto.request.UserLoginReq;
import com.jooany.letsdeal.controller.dto.request.UserUpdateNickReq;
import com.jooany.letsdeal.controller.dto.request.UserUpdatePwReq;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.UserJoinRes;
import com.jooany.letsdeal.controller.dto.response.UserTokensRes;
import com.jooany.letsdeal.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public Response<UserJoinRes> join(@RequestBody @Valid UserJoinReq request) {
		UserDto userDto = userService.join(request.getUserName(), request.getPassword(), request.getNickname());
		return Response.success(UserJoinRes.fromUserDto(userDto));
	}

	@PostMapping("/login")
	public Response<UserTokensRes> login(@RequestBody @Valid UserLoginReq request) {
		AuthTokens authTokens = userService.login(request.getUserName(), request.getPassword());
		return Response.success(UserTokensRes.fromAuthTokens(authTokens));
	}

	@PostMapping("/tokens")
	public Response<UserTokensRes> tokens(HttpServletRequest request) {
		AuthTokens authTokens = userService.generateTokens((String)request.getAttribute("userName"));
		return Response.success(UserTokensRes.fromAuthTokens(authTokens));
	}

	@DeleteMapping
	public Response<Void> withdrawal(Authentication authentication) {
		userService.delete(authentication.getName());
		return Response.success();
	}

	@PostMapping("/updatePw")
	public Response<Void> updatePw(@RequestBody @Valid UserUpdatePwReq request, Authentication authentication) {
		userService.updatePw(request.getBeforePw(), request.getAfterPw(), authentication.getName());
		return Response.success();
	}

	@PostMapping("/updateNick")
	public Response<Void> updateNick(@RequestBody @Valid UserUpdateNickReq request, Authentication authentication) {
		userService.updateNick(request.getNickname(), authentication.getName());
		return Response.success();
	}

}
