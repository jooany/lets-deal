package com.jooany.letsdeal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig {

    private TokenProperties accessToken;
    private TokenProperties refreshToken;

    public String getAccessTokenSecretKey() {
        return accessToken.secretKey;
    }

    public Long getAccessTokenExpiredTimeMs() {
        return accessToken.expiredTimeMs;
    }

    public String getRefreshTokenSecretKey() {
        return refreshToken.secretKey;
    }

    public Long getRefreshTokenExpiredTimeMs() {
        return refreshToken.expiredTimeMs;
    }

    public void setAccessToken(TokenProperties accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(TokenProperties refreshToken) {
        this.refreshToken = refreshToken;
    }

    public static class TokenProperties {
        private String secretKey;
        private Long expiredTimeMs;

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public void setExpiredTimeMs(Long expiredTimeMs) {
            this.expiredTimeMs = expiredTimeMs;
        }
    }
}
