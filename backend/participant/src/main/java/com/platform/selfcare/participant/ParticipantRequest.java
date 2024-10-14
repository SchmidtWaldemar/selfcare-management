package com.platform.selfcare.participant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ParticipantRequest(

		@NotNull(message = "E-Mail should not be empty field")
		@Email(message = "E-Mail should have correct format")
		String email
	) {
}
