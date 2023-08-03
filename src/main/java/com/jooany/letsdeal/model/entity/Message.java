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
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="message_group_id")
    private MessageGroup messageGroup;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name="sender_id")
    private User sender;

    private String messageContent;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @PrePersist
    void registerdAt() { this.registeredAt = Timestamp.from(Instant.now());}

    public static Message of(MessageGroup messageGroup, User receiver, User sender, String messageContent){
        return Message.builder()
                .messageGroup(messageGroup)
                .receiver(receiver)
                .sender(sender)
                .messageContent(messageContent)
                .build();
    }

}
