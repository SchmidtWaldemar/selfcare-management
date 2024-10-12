package com.platform.selfcare.kafka;

import com.platform.selfcare.participant.ParticipantResponse;

public record ParticipantNotification(
		ParticipantResponse participant,
		NotificationType type
	) {
}
