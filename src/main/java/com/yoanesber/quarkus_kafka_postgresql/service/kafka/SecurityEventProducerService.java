package com.yoanesber.quarkus_kafka_postgresql.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.ext.web.RoutingContext;
import io.quarkus.vertx.http.runtime.CurrentVertxRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEvent;

@ApplicationScoped
public class SecurityEventProducerService {

    @Context
    private HttpHeaders headers;

    @Inject
    private CurrentVertxRequest currentVertxRequest;

    private final Emitter<String> securityEventEmitter;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SecurityEventProducerService(@Channel("security-events-outgoing") Emitter<String> securityEventEmitter) {
        this.securityEventEmitter = securityEventEmitter;
    }

    public void sendSecurityEvent(String eventType, String userName, String failMessage) {
        try {
            if (currentVertxRequest == null) {
                throw new IllegalStateException("CurrentVertxRequest is not available.");
            }
            if (securityEventEmitter == null) {
                throw new IllegalStateException("SecurityEventEmitter is not available.");
            }
            
            // Get the current request context
            RoutingContext context = currentVertxRequest.getCurrent();
            String ipAddress = context.request().remoteAddress().host();
            String httpMethod = context.request().method().name();
            String path = context.request().path();
            String userAgent = headers.getHeaderString("User-Agent");

            String jsonPayload = objectMapper.writeValueAsString(
                new SecurityEvent(
                    eventType,
                    userName,
                    ipAddress,
                    userAgent,
                    httpMethod,
                    path,
                    failMessage
                ));

            securityEventEmitter.send(jsonPayload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send security event", e);
        }
    }
}