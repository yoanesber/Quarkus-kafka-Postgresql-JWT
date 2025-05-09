package com.yoanesber.quarkus_kafka_postgresql.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.yoanesber.quarkus_kafka_postgresql.config.serializer.LocalDateTimeSerializer;

@Data
@Setter
@Getter
@RegisterForReflection
@Entity
@Table(name = "security_event")
public class SecurityEvent {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Column(name = "ip_address", nullable = false, length = 50)
    private String ipAddress;

    @Column(name = "user_agent", nullable = false, length = 200)
    private String userAgent;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    @Column(name = "path", length = 200)
    private String path;

    @Column(name = "fail_message", columnDefinition = "text")
    private String failMessage;

    @Column(name = "timestamp", nullable = false, columnDefinition = "timestamp with time zone default now()")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime timestamp;

    public SecurityEvent() {
        // Default constructor
    }

    public SecurityEvent(String eventType, String username, String ipAddress,
                                String userAgent, String httpMethod, String path, 
                                String failMessage) {
        this.eventType = eventType;
        this.username = username;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.httpMethod = httpMethod;
        this.path = path;
        this.failMessage = failMessage;
        this.timestamp = LocalDateTime.now();
    }
}