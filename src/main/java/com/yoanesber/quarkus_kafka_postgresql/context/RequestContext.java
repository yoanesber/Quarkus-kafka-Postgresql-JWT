package com.yoanesber.quarkus_kafka_postgresql.context;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class RequestContext {
    private String attemptedUsername;

    public String getAttemptedUsername() {
        return attemptedUsername;
    }

    public void setAttemptedUsername(String attemptedUsername) {
        this.attemptedUsername = attemptedUsername;
    }
}