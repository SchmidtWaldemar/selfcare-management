package com.platform.selfcare.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantProducer {

	@Autowired
	private KafkaTemplate<String, ParticipantNotification> kafkaTemplate;
	
	public void sendParticipantNotification(ParticipantNotification participantNotification) {
		Message<ParticipantNotification> message = 
				MessageBuilder
					.withPayload(participantNotification)
					.setHeader(KafkaHeaders.TOPIC, "participant-topic")
					.build();
		
		kafkaTemplate.send(message);
	}
}
