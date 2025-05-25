package com.yoanesber.quarkus_kafka_postgresql.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

import com.yoanesber.quarkus_kafka_postgresql.entity.Department;

@ApplicationScoped
public class DepartmentRepository implements PanacheRepository<Department> {
    public Optional<Department> findById(String id) {
        return Optional.ofNullable(find("id", id).firstResult());
    }

    public Optional<Department> findByDeptName(String deptName) {
        return find("LOWER(deptName) = LOWER(?1)", deptName).firstResultOptional();
    }

    public boolean existsById(String id) {
        return find("id", id).count() > 0;
    }

    public boolean existsByDeptName(String deptName) {
        return find("LOWER(deptName) = LOWER(?1)", deptName).count() > 0;
    }
}