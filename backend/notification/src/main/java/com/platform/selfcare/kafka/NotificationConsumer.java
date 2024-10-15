package com.platform.selfcare.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.platform.selfcare.email.EmailService;
import com.platform.selfcare.kafka.group.GroupMessage;
import com.platform.selfcare.kafka.participant.ParticipantMessage;
import com.platform.selfcare.notification.Notification;
import com.platform.selfcare.notification.NotificationRepository;
import com.platform.selfcare.notification.NotificationType;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
	
	private final NotificationRepository repository;
	private final EmailService emailService;
	
	@KafkaListener(topics = "participant-topic", groupId = "participantGroup")
	public void consumeParticipantNotification(ParticipantMessage message) throws MessagingException, jakarta.mail.MessagingException {
		
		repository.save(Notification.builder().type(message.type()).build());
		
		log.info("test consume participant topic with id: " + message.participant().id() + " and type: " + message.type());
		
		if (message.type().equals(NotificationType.REGISTRATION)) {
			emailService.sendRegistrationMail(message.participant().email());
		}
		else if (message.type().equals(NotificationType.NEW_MEMBER_IN_GROUP)) {
			emailService.sendNewGroupMemberMailMessage(message.participant().email());
		}
	}
	
	@KafkaListener(topics = "group-topic", groupId = "groupId")
	public void consumeGroupNotification(GroupMessage message) throws MessagingException {
		
		repository.save(Notification.builder().type(NotificationType.NEW_GROUP).build());
		
		log.info("test consume group topic by name: " + message.group().name());
	}
}