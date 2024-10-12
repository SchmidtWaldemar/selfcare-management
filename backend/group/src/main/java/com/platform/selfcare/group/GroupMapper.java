package com.platform.selfcare.group;

import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public class GroupMapper {
	
	public GroupResponse fromGroup(Group group) {
		return new GroupResponse(
				group.getId(),
				group.getName()
			);
	}

	public Group toGroup(@Valid GroupRequest groupDto) {
		return new Group(groupDto.name());
	}
}
