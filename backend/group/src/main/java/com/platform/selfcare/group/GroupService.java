package com.platform.selfcare.group;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {

	private final GroupRepository repository;
	private final GroupMapper mapper;
	
	public List<GroupResponse> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::fromGroup)
				.collect(Collectors.toList());
	}

}
