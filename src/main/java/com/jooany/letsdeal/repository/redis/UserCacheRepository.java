package com.jooany.letsdeal.repository.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.jooany.letsdeal.controller.dto.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

	private final RedisTemplate<String, UserDto> userRedisTemplate;
	private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

	public void setUser(UserDto userDto) {
		String key = getKey(userDto.getUsername());
		log.info("Set User to Redis, ( Key: {}, UserDto: {} )", key, userDto);
		userRedisTemplate.opsForValue().set(key, userDto, USER_CACHE_TTL);
	}

	public Optional<UserDto> getUserDto(String userName) {
		String key = getKey(userName);
		UserDto userDto = userRedisTemplate.opsForValue().get(key);
		log.info("Get User from Redis, ( Key: {}, UserDto: {} )", key, userDto);
		return Optional.ofNullable(userDto);
	}

	public void deleteUser(String userName) {
		String key = getKey(userName);
		userRedisTemplate.delete(key);
		log.info("Delete User from Redis, ( Key: {} )", key);
	}

	private String getKey(String userName) {
		return "USER:" + userName;
	}
}
