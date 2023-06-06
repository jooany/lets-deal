package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokensRes {
    private String accessToken;
    private String refreshToken;

    public static UserTokensRes fromAuthTokens(AuthTokens authTokens){
        return new UserTokensRes(authTokens.getAccessToken(), authTokens.getRefreshToken());
    }
}
