package com.platform.selfcare.participant;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> {

	Optional<Participant> findByEmail(String email);
}
