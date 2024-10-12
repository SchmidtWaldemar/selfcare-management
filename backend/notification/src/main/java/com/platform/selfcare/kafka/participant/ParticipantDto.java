package com.platform.selfcare.kafka.participant;

public record ParticipantDto(
		Integer id,
		@Deprecated
		String email
	) {
}
