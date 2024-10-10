package com.platform.selfcare.group;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
	
	private final GroupService service;
	
	@GetMapping
	public ResponseEntity<List<GroupResponse>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
}
