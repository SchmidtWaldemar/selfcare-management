package com.platform.selfcare.kafka.participant;

import com.platform.selfcare.notification.NotificationType;

public record ParticipantMessage(
		ParticipantDto participant,
		NotificationType type
	) {
}
