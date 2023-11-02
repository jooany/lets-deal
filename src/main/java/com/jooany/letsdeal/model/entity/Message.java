package com.jooany.letsdeal.model.entity;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "\"messages\"")
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
	@JoinColumn(name = "message_group_id")
	private MessageGroup messageGroup;

	@ManyToOne
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne
	@JoinColumn(name = "receiver_id")
	private User receiver;

	private String messageContent;

	@Builder.Default
	private Boolean wasReadBySender = false;

	@ManyToOne
	@JoinColumn(name = "deleted_by")
	private User deletedBy;

	@Column(name = "registered_at")
	private Timestamp registeredAt;

	@Column(name = "deletedAt")
	private Timestamp deletedAt;

	@PrePersist
	void registerdAt() {
		this.registeredAt = Timestamp.from(Instant.now());
	}

	public static Message of(MessageGroup messageGroup, User sender, String messageContent) {
		return Message.builder()
			.messageGroup(messageGroup)
			.sender(sender)
			.messageContent(messageContent)
			.build();
	}

}
