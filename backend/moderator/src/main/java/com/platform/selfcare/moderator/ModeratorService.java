package com.platform.selfcare.moderator;

import com.platform.selfcare.kafka.ModeratorMessage;
import com.platform.selfcare.kafka.ProducerService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class ModeratorService {

    @Inject
    EntityManager em;

    @Inject
    ProducerService producer;

    @Transactional 
    public Integer createModerator(String firstName, String lastName) {
        Moderator moderator = Moderator.builder()
            .firstName(firstName)
            .lastName(lastName)
            .build();

        em.persist(moderator);
        
        return moderator.getId();
    }

    public void sendMessage(ModeratorMessage response) {
        producer.sendMessage(response);
    }
}