package com.jooany.letsdeal;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import com.jooany.letsdeal.exception.ErrorCode;
import com.jooany.letsdeal.exception.LetsDealAppException;

public class TestUtil {

	public static <T extends LetsDealAppException> T assertException(
		Class<T> expectedType,
		Executable executable,
		ErrorCode errorCode
	) {
		final T exception = Assertions.assertThrows(expectedType, executable);

		assertEquals(errorCode, exception.getErrorCode());

		return exception;
	}

}
