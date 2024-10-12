package com.platform.selfcare.group;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.selfcare.client.RemoteClient;
import com.platform.selfcare.client.ClientResponse;
import com.platform.selfcare.handler.EntityAlreadyExistsException;
import com.platform.selfcare.kafka.GroupNotification;
import com.platform.selfcare.kafka.GroupProducer;
import com.platform.selfcare.member.Member;
import com.platform.selfcare.member.MemberRequest;
import com.platform.selfcare.member.MemberResponse;
import com.platform.selfcare.member.MemberService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

	private final GroupRepository repository;
	private final GroupMapper mapper;
	
	@Autowired
	private final GroupProducer producer;
	
	private final MemberService memberService;
	
	private final RemoteClient remoteClient;
	
	public List<GroupResponse> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::fromGroup)
				.collect(Collectors.toList());
	}

	public GroupResponse create(@Valid GroupRequest groupDto) {
		Optional<Group> found = repository.findByName(groupDto.name());
		
		if (found.isPresent()) {
			throw new EntityAlreadyExistsException("Sorry: Group already exists by this name");
		}
		
		Group group = repository.save(mapper.toGroup(groupDto));
		
		producer.sendGroupNotification(
			new GroupNotification(
				new GroupResponse(group.getId(), group.getName())
			)
		);
		
		return new GroupResponse(group.getId(), group.getName());
	}

	public MemberResponse createMembership(@Valid MemberRequest memberDto) {
		Optional<Group> found = repository.findById(memberDto.groupId());
		
		if (!found.isPresent()) {
			throw new EntityNotFoundException(String.format("Upps: no group found by ID::", memberDto.groupId()));
		}
		
		if (found.get().getMembers()
				.stream()
				.anyMatch(m -> m.getClientId().equals(memberDto.clientNumber()))) {
			throw new EntityAlreadyExistsException("Sorry: Member already exists by group");
		}
		
		Optional<ClientResponse> remoteClientResponse = remoteClient.findRemoteClientById(memberDto.clientNumber());
		
		if (!remoteClientResponse.isPresent()) {
			throw new EntityNotFoundException(String.format("Upps: no client found by Number::", memberDto.clientNumber()));
		}
		
		Integer memberId = memberService.saveMember(memberDto);
		
		if (found.get().getMembers() != null 
				&& found.get().getMembers().size() > 0) {
			
			String[] otherMemberClientIds = found.get().getMembers()
					.stream()
					.filter(m -> !m.getId().equals(memberId))
					.map(Member::getClientId)
					//.map(pId -> pId.toString())
					.toArray(String[]::new);
			
			remoteClient.informOtherMembers(otherMemberClientIds);
		}
		
		return new MemberResponse(memberId);
	}
}
