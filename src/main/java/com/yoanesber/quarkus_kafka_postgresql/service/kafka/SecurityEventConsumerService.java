package com.yoanesber.quarkus_kafka_postgresql.service.kafka;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEvent;
import com.yoanesber.quarkus_kafka_postgresql.service.SecurityEventService;
import jakarta.inject.Inject;

@ApplicationScoped
public class SecurityEventConsumerService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    private SecurityEventService securityEventService;

    private static final Logger LOGGER = Logger.getLogger(SecurityEventConsumerService.class);

    @Incoming("security-events-incoming")
    public void consumeSecurityEvent(String message) {
        // Log the received message
        LOGGER.infof("Received message: %s", message);
        
        if (message == null || message.isEmpty()) {
            LOGGER.error("Received empty or null message.");
            return;
        }

        try {
            // Deserialize JSON message to SecurityEvent object
            SecurityEvent securityEvent = objectMapper.readValue(message, SecurityEvent.class);

            // Validate the security event object
            if (securityEvent == null) {
                LOGGER.error("Failed to parse security event.");
                return;
            }

            // Persist the security event to the database
            securityEventService.persistSecurityEvent(securityEvent);

            // Log the persisted security event
            LOGGER.infof("Security event persisted: %s", securityEvent);
        } catch (Exception e) {
            LOGGER.error("Failed to process security event", e);
        }
    }
}