package com.jooany.letsdeal.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.jooany.letsdeal.controller.dto.UserDto;

import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {
	private final RedisProperties redisProperties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		RedisURI redisURI = RedisURI.create(redisProperties.getHost(), redisProperties.getPort());
		org.springframework.data.redis.connection.RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(
			redisURI);
		LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration);
		factory.afterPropertiesSet();
		return factory;
	}

	@Bean
	public RedisTemplate<String, UserDto> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, UserDto> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<UserDto>(UserDto.class));
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, String> refreshTokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<String>(String.class));
		return redisTemplate;
	}
}
