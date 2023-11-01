package com.jooany.letsdeal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LetsDealAppException extends RuntimeException {

	private ErrorCode errorCode;
	private String message;

	public LetsDealAppException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = null;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return errorCode.getMessage();
		}

		return String.format("%s", message);
	}
}
