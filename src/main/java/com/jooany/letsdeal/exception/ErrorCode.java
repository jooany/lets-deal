package com.jooany.letsdeal.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "이미 사용 중인 ID 입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 사용 중인 닉네임 입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    INVALID_IMAGE_MIME_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다."),
    IMAGE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "이미지를 업로드 할 수 없습니다."),
    EMPTY_IMAGE(HttpStatus.BAD_REQUEST, "최소 1개 이상의 이미지를 등록해야 합니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다."),
    SALE_NOT_FOUND(HttpStatus.NOT_FOUND, "판매글이 존재하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.NOT_FOUND, "권한이 없습니다."),
    PROPOSAL_NOT_FOUND(HttpStatus.NOT_FOUND, "가격 제안건이 존재하지 않습니다.")
    ;

    private HttpStatus status;
    private String message;
}
