package com.jooany.letsdeal.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name ="\"messageGroup\"")
@SequenceGenerator(
        name = "MESSAGE_GROUP_SEQ_GENERATOR",
        sequenceName = "MESSAGE_GROUP_SEQ",
        initialValue = 1, allocationSize = 50
)
public class MessageGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name="buyer_id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name="deleted_by")
    private User deleted_by;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    public static MessageGroup of(Sale sale, User buyer, boolean isDeletedByOneUser){
        return MessageGroup.builder()
                .sale(sale)
                .buyer(buyer)
                .build();
    }
}
