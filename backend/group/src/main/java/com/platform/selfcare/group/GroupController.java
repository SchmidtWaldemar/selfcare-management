package com.platform.selfcare.group;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platform.selfcare.member.MemberRequest;
import com.platform.selfcare.member.MemberResponse;

import jakarta.validation.Valid;
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
	
	@PostMapping("/create")
	public ResponseEntity<GroupResponse> createGroup(@RequestBody @Valid GroupRequest groupDto) {
		return ResponseEntity.ok(service.create(groupDto));
	}
	
	@PostMapping("/createMembership")
	public ResponseEntity<MemberResponse> createMembership(@RequestBody @Valid MemberRequest memberDto) {
		return ResponseEntity.ok(service.createMembership(memberDto));
	}
}
