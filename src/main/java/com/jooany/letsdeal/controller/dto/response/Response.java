package com.jooany.letsdeal.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    public static <T> Response<Void> error(String errorCode) {
        return new Response<>(errorCode, null);
    }

    public static <T> Response<String> error(String errorCode, String message) {
        return new Response<>(errorCode, message);
    }

    public static Response<Void> success() {
        return new Response<Void>("SUCCESS", null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

}
