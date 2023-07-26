package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="\"proposal\"")
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

    @Column(name = "updated_at")
    private Timestamp updateAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    @PreUpdate
    void updatedAt() { this.registeredAt = Timestamp.from(Instant.now());}
}
