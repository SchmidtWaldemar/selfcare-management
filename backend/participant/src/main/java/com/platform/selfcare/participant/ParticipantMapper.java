package com.platform.selfcare.participant;

import org.springframework.stereotype.Service;

@Service
public class ParticipantMapper {
	
	public ParticipantResponse fromParticipant(Participant participant) {
		return new ParticipantResponse(
				participant.getId()
			);
	}
}
