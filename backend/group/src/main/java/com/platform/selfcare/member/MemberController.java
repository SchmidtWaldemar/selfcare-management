package com.platform.selfcare.member;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService service;
	
	@Hidden
	@GetMapping("/group/{group-id}")
	public ResponseEntity<List<MemberResponse>> findByGroupId(@PathVariable("group-id") Integer groupId) {
		return ResponseEntity.ok(service.findAllByGroupId(groupId));
	}
}
