package com.jooany.letsdeal.controller;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import com.jooany.letsdeal.controller.dto.UserDto;
import com.jooany.letsdeal.controller.dto.request.UserJoinReq;
import com.jooany.letsdeal.controller.dto.request.UserLoginReq;
import com.jooany.letsdeal.controller.dto.response.Response;
import com.jooany.letsdeal.controller.dto.response.UserJoinResponse;
import com.jooany.letsdeal.controller.dto.response.UserLoginResponse;
import com.jooany.letsdeal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<UserLoginResponse> login(@RequestBody UserLoginReq request) {
        AuthTokens authTokens = userService.login(request.getUserName(), request.getPassword());
        return Response.success(UserLoginResponse.fromAuthTokens(authTokens));
    }


}
