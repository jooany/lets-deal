package com.jooany.letsdeal.model.entity;

import org.hibernate.Hibernate;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Proposal proposal = (Proposal)o;
        return id != null && id.equals(proposal.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
