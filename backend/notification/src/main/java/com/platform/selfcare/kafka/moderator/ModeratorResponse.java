package com.platform.selfcare.kafka.moderator;

public record ModeratorResponse(
		Integer id,
		String firstName,
		String lastName
	) {
}
