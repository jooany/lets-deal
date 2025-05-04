package com.jooany.letsdeal.util;

import java.util.UUID;

public class TestUtils {
	public static String someAlphaNumericString(int length) {
		return UUID.randomUUID().toString().replace("-", "").substring(0, length);
	}
}
