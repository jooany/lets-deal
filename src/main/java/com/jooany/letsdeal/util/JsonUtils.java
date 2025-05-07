package com.jooany.letsdeal.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JsonUtils {
	private final ObjectMapper objectMapper;

	public String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error("JSON 변환 실패", e);
			throw new LetsDealAppException(ErrorCode.SERVER_ERROR);
		}
	}
}
