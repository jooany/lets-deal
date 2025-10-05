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
	public void consume(String jsonMessage) throws Exception {
		ProposalMessage message = objectMapper.readValue(jsonMessage, ProposalMessage.class);

		redissonLockMaxProposalFacade.saveProposal(
			message.getProductId(),
			message.getBuyerPrice(),
			message.getUsername()
		);
	}
}
