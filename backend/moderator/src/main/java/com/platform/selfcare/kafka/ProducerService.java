package com.platform.selfcare.kafka;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ProducerService {

    @Inject
    @Channel("moderator-out")
    Emitter<String> emitter;

    public void sendMessage(ModeratorMessage response) {
        ObjectMapper mapper = new ObjectMapper();
		
		String message = null;
		try {
			message = mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

        emitter.send(message);
    }
}