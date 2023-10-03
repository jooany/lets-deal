package com.jooany.letsdeal.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateReq {

    private String beforePw;
    private String afterPw;
    private String nickname;
}
