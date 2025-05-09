package com.yoanesber.quarkus_kafka_postgresql.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@RegisterForReflection
public class AuthLoginDTO {
    String userName;
    String password;
}
