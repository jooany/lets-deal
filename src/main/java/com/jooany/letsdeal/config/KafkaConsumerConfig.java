package com.jooany.letsdeal.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.ExponentialBackOff;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> template) {
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template,
			(record, ex) -> new TopicPartition(record.topic() + ".dlt", -1));

		ExponentialBackOff backOff = new ExponentialBackOff(1000L, 2.0);
		backOff.setMaxAttempts(4);

		return new DefaultErrorHandler(recoverer, backOff);
	}
}