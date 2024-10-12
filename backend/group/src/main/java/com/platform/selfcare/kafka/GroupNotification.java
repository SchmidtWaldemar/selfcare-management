package com.platform.selfcare.kafka;

import com.platform.selfcare.group.GroupResponse;

public record GroupNotification(
		GroupResponse group
	) {
}
