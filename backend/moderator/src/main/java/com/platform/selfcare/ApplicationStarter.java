package com.platform.selfcare;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;

import com.platform.selfcare.kafka.ModeratorMessage;
import com.platform.selfcare.kafka.ModeratorRequest;
import com.platform.selfcare.kafka.ModeratorResponse;
import com.platform.selfcare.moderator.ModeratorService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/moderators")
public class ApplicationStarter {
	
	@Inject
	ModeratorService service;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create")
	public RestResponse<ModeratorResponse> sendMessage(ModeratorRequest request) {
		Integer moderatorId = service.createModerator(request.firstName(), request.lastName());
		ModeratorResponse response = new ModeratorResponse(moderatorId, request.firstName(), request.lastName());
		service.sendMessage(new ModeratorMessage(response));
		return ResponseBuilder.ok(response, MediaType.APPLICATION_JSON).build();
	}
}