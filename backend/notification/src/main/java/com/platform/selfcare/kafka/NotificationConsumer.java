package com.platform.selfcare.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {
	
	@KafkaListener(topics = "participant-topic", groupId = "participantGroup")
	public void consumeParticipantNotification() throws MessagingException {
		System.out.println("test consume participant topic");
		// TODO implement mail sender here
	}
}