package com.platform.selfcare.client;

import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = "participant-service",
	url = "${application.config.participant-url}"
)
public interface RemoteClient {
	
	@GetMapping("/status/{client-id}")
	Optional<ClientResponse> findRemoteClientById(@PathVariable("client-id") String clientId);
	
	@GetMapping("/info/{clientIds}")
	void informOtherMembers(@PathVariable("clientIds") String[] clientIds);
}
