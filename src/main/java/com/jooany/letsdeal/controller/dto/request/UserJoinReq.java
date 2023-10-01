package com.jooany.letsdeal.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinReq {
    private String userName;
    private String password;
    private String nickname;
}
