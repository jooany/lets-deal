package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.controller.dto.request.UserLoginReq;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.UserJoinResponse;
import com.jooany.letsdeal.controller.dto.response.UserTokensResponse;
import com.jooany.letsdeal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinReq request) {
        UserDto userDto = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUserDto(userDto));
    }

    @PostMapping("/login")
    public Response<UserTokensResponse> login(@RequestBody UserLoginReq request) {
        AuthTokens authTokens = userService.login(request.getUserName(), request.getPassword());
        return Response.success(UserTokensResponse.fromAuthTokens(authTokens));
    }

    @PostMapping("/tokens")
    public Response<UserTokensResponse> tokens(HttpServletRequest request) {
        AuthTokens authTokens = userService.generateTokens((String) request.getAttribute("userName"));
        return Response.success(UserTokensResponse.fromAuthTokens(authTokens));
    }


}
