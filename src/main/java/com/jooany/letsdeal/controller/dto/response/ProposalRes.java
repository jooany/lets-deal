package com.jooany.letsdeal.controller.dto.response;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

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
    private Timestamp registeredAt;
    private Timestamp updateAt;

    @QueryProjection
    public ProposalRes(Long id, Long saleId, Long userId, String userName, Integer buyerPrice, ProposalStatus proposalStatus, Boolean isProposedByCurrentUser, Timestamp registeredAt, Timestamp updateAt) {
        this.id = id;
        this.saleId = saleId;
        this.userId = userId;
        this.userName = userName;
        this.buyerPrice = buyerPrice;
        this.proposalStatus = proposalStatus;
        this.isProposedByCurrentUser = isProposedByCurrentUser;
        this.registeredAt = registeredAt;
        this.updateAt = updateAt;
    }

//    public static ProposalRes fromEntity(Proposal proposal){
//        return ProposalRes.builder()
//                .id(proposal.getId())
//                .saleId(proposal.getSale().getId())
//                .userId(proposal.getUser().getId())
//                .userName(proposal.getUser().getUserName())
//                .buyerPrice(proposal.getBuyerPrice())
//                .proposalStatus(proposal.getProposalStatus())
//                .isProposedByCurrentUser(proposal.get)
//                .registeredAt(proposal.getRegisteredAt())
//                .updateAt(proposal.getUpdateAt())
//                .build();
//    }
}
