package com.platform.selfcare.group;

import org.springframework.stereotype.Service;

@Service
public class GroupMapper {
	
	public GroupResponse fromGroup(Group group) {
		return new GroupResponse(
				group.getId()
			);
	}
}
