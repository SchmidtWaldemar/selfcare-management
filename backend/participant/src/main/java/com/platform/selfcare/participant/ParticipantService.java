package com.platform.selfcare.participant;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platform.selfcare.handler.EntityAlreadyExistsException;
import com.platform.selfcare.kafka.NotificationType;
import com.platform.selfcare.kafka.ParticipantNotification;
import com.platform.selfcare.kafka.ParticipantProducer;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipantService {

	private final ParticipantRepository repository;
	private final ParticipantMapper mapper;
	
	@Autowired
	private final ParticipantProducer producer;
	
	public List<ParticipantResponse> findAll() {
		return repository.findAll()
				.stream()
				.map(mapper::fromParticipant)
				.collect(Collectors.toList());
	}

	public ParticipantResponse register(@Valid ParticipantRequest participantDto) {
		Optional<Participant> found = repository.findByEmail(participantDto.email());
		if (found.isPresent()) {
			throw new EntityAlreadyExistsException("Sorry: Participant already exists");
		}
		Participant participant = repository.save(mapper.toParticipant(participantDto));
		
		producer.sendParticipantNotification(
			new ParticipantNotification(
				new ParticipantResponse(
					participant.getId()
				),
				NotificationType.REGISTRATION
			)
		);
		
		return new ParticipantResponse(participant.getId());
	}

	public ParticipantResponse findById(Integer participantId) {
		Optional<Participant> found = repository.findById(participantId);
		if (!found.isPresent()) {
			throw new EntityNotFoundException(String.format("Sorry: Participant not found by ID::", participantId));
		}
		
		return new ParticipantResponse(found.get().getId());
	}

	public void sendMembershipMailToParticipants(String[] clientIds) {
		for (String clientId : clientIds) {
			
			Optional<Participant> found = repository.findById(Integer.valueOf(clientId));
			if (!found.isPresent()) {
				log.warn(String.format("Sorry: Participant not found by ID::", clientId));
				continue;
			}
			
			producer.sendParticipantNotification(
				new ParticipantNotification(
					new ParticipantResponse(
						found.get().getId()
					),
					NotificationType.NEW_MEMBER_IN_GROUP
				)
			);
		}
	}
}
