package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;

    public static UserLoginResponse fromAuthTokens(AuthTokens authTokens){
        return new UserLoginResponse(authTokens.getAccessToken(), authTokens.getRefreshToken());
    }
}
