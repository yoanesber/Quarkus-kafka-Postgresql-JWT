package com.yoanesber.quarkus_kafka_postgresql.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Getter
@Setter
@RegisterForReflection
public class DepartmentDTO {
    private String id;
    private String deptName;
    private boolean active;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long createdBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long updatedBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime updatedAt;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long deletedBy;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private LocalDateTime deletedAt;
}
