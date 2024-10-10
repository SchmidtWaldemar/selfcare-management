package com.platform.selfcare.participant;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
	
	private final ParticipantService service;
	
	@GetMapping
	public ResponseEntity<List<ParticipantResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
}
