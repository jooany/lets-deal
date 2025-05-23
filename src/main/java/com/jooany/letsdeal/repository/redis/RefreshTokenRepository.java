package com.jooany.letsdeal.repository.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

	private final RedisTemplate<String, String> refreshTokenRedisTemplate;

	public void setRefreshToken(String userName, String refreshToken, Long expiredTimeMs) {
		String key = getKey(userName);
		log.info("Set refresh-token to Redis, ( Key: {}, RefreshToken: {} )", key, refreshToken);
		refreshTokenRedisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expiredTimeMs));
	}

	public Optional<String> getRefreshToken(String userName) {
		String key = getKey(userName);
		String refreshToken = refreshTokenRedisTemplate.opsForValue().get(key);
		log.info("Get refresh-token from Redis, ( Key: {}, RefreshToken: {} )", key, refreshToken);
		return Optional.ofNullable(refreshToken);
	}

	public void deleteUser(String userName) {
		String key = getKey(userName);
		refreshTokenRedisTemplate.delete(key);
		log.info("Delete User from Redis, ( Key: {} )", key);
	}

	private String getKey(String userName) {
		return "TOKEN:" + userName;
	}
}
