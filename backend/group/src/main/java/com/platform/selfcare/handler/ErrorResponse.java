package com.platform.selfcare.handler;

import java.util.Map;

public record ErrorResponse(
	
		Map<String, String> errors
		
	) {
}
