package com.platform.selfcare.kafka;

public record ParticipantNotification(
		ParticipantNotificationResponse participant,
		NotificationType type
	) {
}
