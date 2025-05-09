package com.yoanesber.quarkus_kafka_postgresql.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Embeddable
public class UserRoleId {
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Integer roleId;
}
