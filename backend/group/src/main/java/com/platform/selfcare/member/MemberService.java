package com.platform.selfcare.member;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository repository;
	private final MemberMapper mapper;
	
	public Integer saveMember(MemberRequest request) {
		Member member = mapper.toMember(request);
		return repository.save(member).getId();
	}

	public List<MemberResponse> findAllByGroupId(Integer groupId) {
		return repository.findAllByGroupId(groupId)
				.stream()
				.map(mapper::toMemberResponse)
				.collect(Collectors.toList());
	}
}
