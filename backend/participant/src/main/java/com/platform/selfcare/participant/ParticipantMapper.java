package com.platform.selfcare.participant;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class ParticipantMapper {
	
	public ParticipantResponse fromParticipant(Participant participant) {
		return new ParticipantResponse(
			participant.getId()
		);
	}

	public Participant toParticipant(@Valid ParticipantRequest participantDto) {
		return new Participant(participantDto.email());
	}
}
