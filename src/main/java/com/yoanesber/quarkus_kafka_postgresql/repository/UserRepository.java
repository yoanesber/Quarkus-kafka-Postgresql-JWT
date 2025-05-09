package com.yoanesber.quarkus_kafka_postgresql.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.time.Instant;

import com.yoanesber.quarkus_kafka_postgresql.entity.User;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    public Optional<User> findByUserName(String userName) {
        return find("LOWER(userName) = LOWER(?1)", userName).firstResultOptional();
    }

    public boolean existsByUserName(String userName) {
        return find("LOWER(userName) = LOWER(?1)", userName).count() > 0;
    }

    @Transactional
    public boolean updateLastLogin(Long userId) {
        User user = findById(userId);
        if (user == null) {
            return false;
        }

        user.setLastLogin(Instant.now());
        persist(user);
        return true;
    }
}