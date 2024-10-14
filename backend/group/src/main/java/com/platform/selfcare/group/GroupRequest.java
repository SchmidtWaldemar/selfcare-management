package com.platform.selfcare.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GroupRequest(
		Integer id,
		
		@NotNull(message = "Name can not be empty")
		@NotBlank(message = "Name can not be empty")
		String name
	) {
}
