package com.yoanesber.quarkus_kafka_postgresql.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import com.yoanesber.quarkus_kafka_postgresql.entity.SecurityEvent;

@ApplicationScoped
public class SecurityEventRepository implements PanacheRepository<SecurityEvent> {}