package com.jooany.letsdeal.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginReq {
    private String userName;
    private String password;
}
