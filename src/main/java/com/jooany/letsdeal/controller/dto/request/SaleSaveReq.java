package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SaleSaveReq {
    @NotNull(message = "NULL일 수 없습니다.")
    private Long categoryId;

    @NotBlank(message = "비어있을 수 없습니다.")
    @Size(max = 30, message = "30자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "비어있을 수 없습니다.")
    @Size(max = 300, message = "300자 이하여야 합니다.")
    private String contents;

    private Integer sellerPrice;
}
