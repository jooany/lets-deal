package com.jooany.letsdeal.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "messages")
@SequenceGenerator(
        name = "MESSAGES_SEQ_GENERATOR",
        sequenceName = "MESSAGES_SEQ"
)
public class Message extends SoftDeletableBaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "message_group_id", nullable = false)
    private MessageGroup messageGroup;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String messageContent;

    @ManyToOne
    @JoinColumn(name = "deleted_by")
    private User deletedBy;
}
