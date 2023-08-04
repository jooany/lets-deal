package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name ="\"proposal\"")
@SequenceGenerator(
        name = "PROPOSAL_SEQ_GENERATOR",
        sequenceName = "PROPOSAL_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 구매자
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="sale_id")
    private Sale sale;

    @Column(name="buyer_price")
    private Integer buyerPrice;

    @Column(name="proposal_status")
    @Enumerated(EnumType.STRING)
    private ProposalStatus proposalStatus;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    public static Proposal of(User user, Sale sale, Integer buyerPrice){
        return Proposal.builder()
                .user(user)
                .sale(sale)
                .buyerPrice(buyerPrice)
                .proposalStatus(ProposalStatus.REQUESTING)
                .build();
    }

}
