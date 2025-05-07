package com.jooany.letsdeal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig {

    private TokenProperties accessToken;
    private TokenProperties refreshToken;
    private List<String> excludedPaths;

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

    public List<String> getExcludedPaths() {
        return excludedPaths;
    }

    public void setAccessToken(TokenProperties accessToken) {
        this.accessToken = accessToken;
    }

    public void setRefreshToken(TokenProperties refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setExcludedPaths(List<String> excludedPaths) {
        this.excludedPaths = excludedPaths;
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
