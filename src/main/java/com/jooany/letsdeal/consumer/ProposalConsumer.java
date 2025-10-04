package com.jooany.letsdeal.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jooany.letsdeal.controller.dto.ProposalMessage;
import com.jooany.letsdeal.facade.RedissonLockMaxProposalFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProposalConsumer {
	private final ObjectMapper objectMapper;
	private final RedissonLockMaxProposalFacade redissonLockMaxProposalFacade;

	@KafkaListener(topics = "proposal-topic", groupId = "lets-deal-group")
	public void consume(String jsonMessage) {
		try {
			ProposalMessage message = objectMapper.readValue(jsonMessage, ProposalMessage.class);
			log.info("Consumed message for productId: {}", message.getProductId());

			redissonLockMaxProposalFacade.saveProposal(
				message.getProductId(),
				message.getBuyerPrice(),
				message.getUsername()
			);

		} catch (Exception e) {
			log.error("Failed to process message: {}", jsonMessage, e);
			// TODO: 에러 처리 로직
		}
	}
}
