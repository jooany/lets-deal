package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;


public record UserLoginReq(
        @NotBlank(message = "비어있을 수 없습니다.") String userName,
        @NotBlank(message = "비어있을 수 없습니다.") String password

) {
}
