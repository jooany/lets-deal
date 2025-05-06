package com.jooany.letsdeal.controller.dto;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class MyProposalRes {
    private Long id;
    private Long saleId;
    private Integer buyerPrice;
    private ProposalStatus proposalStatus;
    private LocalDateTime registeredAt;

    @QueryProjection
    public MyProposalRes(
            Long id,
            Long saleId,
            Integer buyerPrice,
            ProposalStatus proposalStatus,
            LocalDateTime registeredAt
    ) {
        this.id = id;
        this.saleId = saleId;
        this.buyerPrice = buyerPrice;
        this.proposalStatus = proposalStatus;
        this.registeredAt = registeredAt;
    }
}
