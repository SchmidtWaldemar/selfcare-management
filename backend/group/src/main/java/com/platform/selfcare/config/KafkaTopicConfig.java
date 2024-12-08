package com.platform.selfcare.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
	
	@Bean
	NewTopic groupTopic() {
		return TopicBuilder.name("group-topic").build();
	}

    /* 
     
	@Bean
    KafkaAdmin admin(@Value("${spring.kafka.bootstrap-servers}") String kafkaBrokers) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        return new KafkaAdmin(configs);
    }
    */
    
}
