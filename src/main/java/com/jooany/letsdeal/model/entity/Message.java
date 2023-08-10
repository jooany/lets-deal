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
@Table(name ="\"messages\"")
@SequenceGenerator(
        name = "MESSAGE_SEQ_GENERATOR",
        sequenceName = "MESSAGE_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="message_group_id")
    private MessageGroup messageGroup;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    private String messageContent;

    @Builder.Default
    private Boolean wasReadBySender = false;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    public static Message of(MessageGroup messageGroup, User sender, String messageContent){
        return Message.builder()
                .messageGroup(messageGroup)
                .sender(sender)
                .messageContent(messageContent)
                .build();
    }

}
