package com.yoanesber.quarkus_kafka_postgresql.service;

import io.smallrye.jwt.build.Jwt;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {
    public String generateToken(String userName, Set<String> roles) {
        Config config = ConfigProvider.getConfig();
        String jwt_issuer = config.getValue("mp.jwt.verify.issuer", String.class);
        long token_age = config.getValue("mp.jwt.verify.token.age", long.class);

        return Jwt.issuer(jwt_issuer) // Set the issuer of the token; The issuer is the entity that creates the token.
            .upn(userName) // Set the subject of the token; The subject is the user for whom the token is issued.
            .subject(userName) // Set the subject of the token; The subject is typically the same as the issuer.
            .groups(roles) // Set the groups of the token; The groups are the roles or permissions associated with the user.
            .expiresAt(Instant.now().plus(token_age, ChronoUnit.HOURS)) // Set the expiration time of the token; The expiration time is when the token will no longer be valid.
            .issuedAt(java.time.Instant.now()) // Set the issued time of the token; The issued time is when the token was created.
            .sign();
    }
}
