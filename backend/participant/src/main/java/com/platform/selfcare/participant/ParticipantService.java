package com.platform.selfcare.participant;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipantService {

	private final ParticipantRepository repository;
	private final ParticipantMapper mapper;
	
	public List<ParticipantResponse> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::fromParticipant)
				.collect(Collectors.toList());
	}

}
