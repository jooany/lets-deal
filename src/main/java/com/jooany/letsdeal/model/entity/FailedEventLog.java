package com.jooany.letsdeal.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "failed_event_logs")
@SequenceGenerator(
	name = "FAILED_EVENT_LOGS_SEQ_GENERATOR",
	sequenceName = "FAILED_EVENT_LOGS_SEQ"
)
public class FailedEventLog extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAILED_EVENT_LOGS_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false)
	private String topic;

	@Lob
	@Column(nullable = false, columnDefinition = "TEXT")
	private String messagePayload;

	@Lob
	@Column(columnDefinition = "TEXT")
	private String errorReason;

	@Builder
	public FailedEventLog(String topic, String messagePayload, String errorReason) {
		this.topic = topic;
		this.messagePayload = messagePayload;
		this.errorReason = errorReason;
	}
}
