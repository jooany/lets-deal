package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ProposalRes {
    private Long id;
    private Long saleId;
    private Long userId;
    private String userName;
    private Integer buyerPrice;
    private ProposalStatus proposalStatus;
    private boolean isProposedByCurrentUser;
    private LocalDateTime registeredAt;

    @QueryProjection
    public ProposalRes(
            Long id,
            Long saleId,
            Long userId,
            String userName,
            Integer buyerPrice,
            ProposalStatus proposalStatus,
            Boolean isProposedByCurrentUser,
            LocalDateTime registeredAt
    ) {
        this.id = id;
        this.saleId = saleId;
        this.userId = userId;
        this.userName = userName;
        this.buyerPrice = buyerPrice;
        this.proposalStatus = proposalStatus;
        this.isProposedByCurrentUser = isProposedByCurrentUser;
        this.registeredAt = registeredAt;
    }
}
