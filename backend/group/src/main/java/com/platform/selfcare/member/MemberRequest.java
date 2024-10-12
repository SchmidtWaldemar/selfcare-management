package com.platform.selfcare.member;

public record MemberRequest(
		Integer id, 
		Integer groupId,
		String clientNumber,
		String nickname
	) {
}
