package com.yoanesber.quarkus_kafka_postgresql.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEvent;
import com.yoanesber.quarkus_kafka_postgresql.repository.SecurityEventRepository;

@ApplicationScoped
public class SecurityEventService {
    @Inject
    private SecurityEventRepository securityEventRepository;

    @Transactional
    public SecurityEvent persistSecurityEvent(SecurityEvent securityEvent) {
        if (securityEvent == null) {
            throw new IllegalArgumentException("SecurityEvent cannot be null.");
        }

        securityEventRepository.persist(securityEvent);
        return securityEvent;
    }
}