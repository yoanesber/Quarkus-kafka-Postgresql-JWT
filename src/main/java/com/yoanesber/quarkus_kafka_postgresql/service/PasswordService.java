package com.yoanesber.quarkus_kafka_postgresql.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class PasswordService {
    // To compare the password with the hashed password
    // This method is used to verify the password entered by the user during login
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword, hashedPassword);
    }

    // To hash the password before storing it in the database
    // This method is used to hash the password when the user registers or changes their password
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
