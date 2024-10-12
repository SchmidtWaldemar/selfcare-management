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
public class GroupProducer {

	@Autowired
	private KafkaTemplate<String, GroupNotification> kafkaTemplate;
	
	public void sendGroupNotification(GroupNotification groupNotification) {
		Message<GroupNotification> message = 
				MessageBuilder
					.withPayload(groupNotification)
					.setHeader(KafkaHeaders.TOPIC, "group-topic")
					.build();
		
		kafkaTemplate.send(message);
	}
}
