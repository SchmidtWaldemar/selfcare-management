package com.platform.selfcare.participant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "participant")
public class Participant {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String email;
	
	public Participant(String email) {
		this.email = email;
	}
}
