package com.jooany.letsdeal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtTokenConfig {

	private AccessToken accessToken;
	private RefreshToken refreshToken;

	@Getter
	@Setter
	public static class AccessToken {
		private String secretKey;
		private Long expiredTimeMs;
	}

	@Getter
	@Setter
	public static class RefreshToken {
		private String secretKey;
		private Long expiredTimeMs;
	}
}
