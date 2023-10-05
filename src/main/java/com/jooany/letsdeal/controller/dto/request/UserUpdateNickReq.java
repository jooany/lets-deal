package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateNickReq {
    @NotBlank(message = "비어있을 수 없습니다.")
    @Size(min = 2, max = 10, message = "2자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]*[ㄱ-ㅎㅏ-ㅣ]*$", message = "한글, 영문자, 숫자, 특수문자[_] 만으로 구성되어야 합니다.")
    private String nickname;
}
