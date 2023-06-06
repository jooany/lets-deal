package com.jooany.letsdeal.model.entity;

import com.jooany.letsdeal.model.enumeration.ProposalStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name ="\"proposal\"")
@SQLDelete(sql = "UPDATE \"proposal\" SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is NULL")
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

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    @PreUpdate
    void updatedAt() { this.registeredAt = Timestamp.from(Instant.now());}
}
