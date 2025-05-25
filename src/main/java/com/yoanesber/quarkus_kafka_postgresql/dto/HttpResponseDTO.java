package com.yoanesber.quarkus_kafka_postgresql.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@RegisterForReflection
public class HttpResponseDTO {
    private String message;
    private String error;
    private String path;
    private Integer status;
    private Object data;
    private Instant timestamp;

    public HttpResponseDTO(String message, String error, String path, Integer status, Object data) {
        this.message = message;
        this.error = error;
        this.path = path;
        this.status = status;
        this.data = data;
        this.timestamp = Instant.now();
    }
}
