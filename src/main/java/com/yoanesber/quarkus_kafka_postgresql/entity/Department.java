package com.yoanesber.quarkus_kafka_postgresql.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@RegisterForReflection
@Entity
@Table(name = "department", 
       uniqueConstraints = @UniqueConstraint(name = "idx_16979_dept_name", columnNames = "dept_name"))
public class Department {

    @Id
    @Column(length = 4, nullable = false)
    private String id;

    @Column(name = "dept_name", length = 40, nullable = false, unique = true)
    private String deptName;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_date", nullable = false, columnDefinition = "timestamp with time zone default now()")
    private LocalDateTime createdDate;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "updated_date", nullable = false, columnDefinition = "timestamp with time zone default now()")
    private LocalDateTime updatedDate;
}
