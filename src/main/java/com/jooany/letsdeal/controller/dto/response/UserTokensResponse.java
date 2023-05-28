package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.controller.dto.AuthTokens;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserTokensResponse {
    private String accessToken;
    private String refreshToken;

    public static UserTokensResponse fromAuthTokens(AuthTokens authTokens){
        return new UserTokensResponse(authTokens.getAccessToken(), authTokens.getRefreshToken());
    }
}
