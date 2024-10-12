package com.platform.selfcare.member;

import org.springframework.stereotype.Service;

import com.platform.selfcare.group.Group;

@Service
public class MemberMapper {

	public Member toMember(MemberRequest request) {
		
		return Member.builder()
				.id(request.id())
				.group(
					Group.builder()
					.id(request.groupId())
					.build())
				.clientId(request.clientNumber())
				.nickname(request.nickname())
				.build();
	}
	
	public MemberResponse toMemberResponse(Member member) {
		return new MemberResponse(member.getId());
	}

}
