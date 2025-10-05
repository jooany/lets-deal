package com.jooany.letsdeal.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jooany.letsdeal.model.entity.FailedEventLog;
import com.jooany.letsdeal.repository.FailedEventLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProposalDltConsumer {
	private final FailedEventLogRepository failedEventLogRepository;

	@KafkaListener(topics = "proposal-topic.dlt", groupId = "lets-deal-dlt-group")
	public void consumeDlt(String message) {
		log.error("[DLT Consumer] 가격제안 최종 실패 메시지를 수신했습니다. DB에 기록합니다: {}", message);

		FailedEventLog log = FailedEventLog.builder()
			.topic("proposal-topic.dlt")
			.messagePayload(message)
			.errorReason("가격제안에서 모든 재시도에 실패했습니다.")
			.build();

		failedEventLogRepository.save(log);
	}
}
