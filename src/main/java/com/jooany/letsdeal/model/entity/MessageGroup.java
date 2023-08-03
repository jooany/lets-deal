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

    private boolean isDeletedByOneUser;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    public static MessageGroup of(Sale sale, User buyer, boolean isDeletedByOneUser){
        return MessageGroup.builder()
                .sale(sale)
                .buyer(buyer)
                .isDeletedByOneUser(isDeletedByOneUser)
                .build();
    }
}
