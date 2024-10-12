package com.platform.selfcare.group;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {

	Optional<Group> findByName(String name);

}
