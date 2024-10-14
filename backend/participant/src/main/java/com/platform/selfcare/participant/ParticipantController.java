package com.platform.selfcare.participant;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
	
	private final ParticipantService service;
	
	@Hidden
	@GetMapping
	public ResponseEntity<List<ParticipantResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<ParticipantResponse> register(@RequestBody @Valid ParticipantRequest participantDto) {
		return ResponseEntity.ok(service.register(participantDto));
	}
	
	@Hidden
	@GetMapping("/{client-id}")
	public ResponseEntity<ParticipantResponse> findById(@PathVariable("client-id") String clientId) {
		return ResponseEntity.ok(service.findById(Integer.valueOf(clientId)));
	}
	
	@Hidden
	@GetMapping("/info/{clientIds}")
	public ResponseEntity<Void> informOtherMembers(@PathVariable("clientIds") String[] clientIds) {
		service.sendMembershipMailToParticipants(clientIds);
		return ResponseEntity.accepted().build();
	}
}
