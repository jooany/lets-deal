package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.controller.dto.request.UserLoginReq;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.UserJoinRes;
import com.jooany.letsdeal.controller.dto.response.UserTokensRes;
import com.jooany.letsdeal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/join")
    public Response<UserJoinRes> join(@RequestBody UserJoinReq request) {
        UserDto userDto = userService.join(request.getUserName(), request.getPassword());
        return Response.success(UserJoinRes.fromUserDto(userDto));
    }

    @PostMapping("/login")
    public Response<UserTokensRes> login(@RequestBody UserLoginReq request) {
        AuthTokens authTokens = userService.login(request.getUserName(), request.getPassword());
        return Response.success(UserTokensRes.fromAuthTokens(authTokens));
    }

    @PostMapping("/tokens")
    public Response<UserTokensRes> tokens(HttpServletRequest request) {
        AuthTokens authTokens = userService.generateTokens((String) request.getAttribute("userName"));
        return Response.success(UserTokensRes.fromAuthTokens(authTokens));
    }

    @DeleteMapping
    public Response<Void> withdrawal(Authentication authentication) {
        userService.delete(authentication.getName());
        return Response.success();
    }


}
