package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.controller.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    // ( key : userName포함키, value : userDto) 형태로 redis에 저장
    private final RedisTemplate<String, UserDto> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(UserDto userDto){
        String key = getKey(userDto.getUsername());
        log.info("Set User to Redis, ( Key: {}, UserDto: {} )", key, userDto);
        userRedisTemplate.opsForValue().setIfAbsent(key, userDto, USER_CACHE_TTL);
    }

    public Optional<UserDto> getUserDto(String userName){
        String key = getKey(userName);
        UserDto userDto = userRedisTemplate.opsForValue().get(key);
        log.info("Get User from Redis, ( Key: {}, UserDto: {} )",key, userDto);
        return Optional.ofNullable(userDto);
    }

    public void deleteUser(String userName){
        String key = getKey(userName);
        userRedisTemplate.delete(key);
        log.info("Delete User from Redis, ( Key: {} )",key);
    }

    private String getKey(String userName){
        return "USER:" + userName;
    }
}
