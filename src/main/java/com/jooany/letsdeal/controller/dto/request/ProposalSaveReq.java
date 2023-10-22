package com.jooany.letsdeal.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalSaveReq {
    @NotNull(message = "NULL일 수 없습니다.")
    @Positive(message = "양수이어야 합니다.")
    private Integer buyerPrice;
}
