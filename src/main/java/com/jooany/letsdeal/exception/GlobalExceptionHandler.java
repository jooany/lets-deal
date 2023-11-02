package com.jooany.letsdeal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jooany.letsdeal.controller.dto.response.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(LetsDealAppException.class)
	public ResponseEntity<?> applicationHandler(LetsDealAppException e) {
		log.error("Error occurs {}", e.toString());

		if (e.getMessage() != null) {
			return ResponseEntity.status(e.getErrorCode().getStatus())
				.body(Response.error(e.getErrorCode().name(), e.getMessage()));
		}

		return ResponseEntity.status(e.getErrorCode().getStatus())
			.body(Response.error(e.getErrorCode().name()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response<String> validExceptionHandler(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		FieldError fieldError = bindingResult.getFieldError();
		String bindResultCode = fieldError.getCode();

		String fieldName = fieldError.getField();
		String errorMessage = fieldError.getDefaultMessage();

		return new Response<>(bindResultCode, "[" + fieldName + "]" + " " + errorMessage);

	}
}
