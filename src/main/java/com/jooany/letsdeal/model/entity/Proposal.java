package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "proposals")
@SequenceGenerator(
        name = "PROPOSALS_SEQ_GENERATOR",
        sequenceName = "PROPOSALS_SEQ"
)
public class Proposal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROPOSALS_SEQ_GENERATOR")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(nullable = false)
    private Integer buyerPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposalStatus proposalStatus = ProposalStatus.REQUESTING;

    @Builder(builderMethodName = "proposalBuilder")
    private Proposal(User buyer, Sale sale, Integer buyerPrice) {
        this.buyer = buyer;
        this.sale = sale;
        this.buyerPrice = buyerPrice;
    }

    public static ProposalBuilder builder(User buyer, Sale sale, Integer buyerPrice) {
        return proposalBuilder()
                .buyer(buyer)
                .sale(sale)
                .buyerPrice(buyerPrice);
    }

    public void updateProposalStatus(ProposalStatus proposalStatus) {
        this.proposalStatus = proposalStatus;
    }

}
