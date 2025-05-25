package com.yoanesber.quarkus_kafka_postgresql.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;

import com.yoanesber.quarkus_kafka_postgresql.entity.Department;
import com.yoanesber.quarkus_kafka_postgresql.exception.DepartmentAlreadyExistsException;
import com.yoanesber.quarkus_kafka_postgresql.exception.DepartmentNotFoundException;
import com.yoanesber.quarkus_kafka_postgresql.repository.DepartmentRepository;

@ApplicationScoped
public class DepartmentService {
    @Inject
    private DepartmentRepository departmentRepository;

    @Transactional
    public Department createDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }

        // Validate the department object here if needed

        // Check if the department already exists by ID or name
        if (departmentRepository.existsById(department.getId())) {
            throw new DepartmentAlreadyExistsException("Department with ID " + department.getId() + " already exists.");
        }

        // Check if the department already exists by name
        if (departmentRepository.existsByDeptName(department.getDeptName())) {
            throw new DepartmentAlreadyExistsException("Department with name " + department.getDeptName() + " already exists.");
        }

        // Always set createdBy and updatedBy to the same value for new departments
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedBy(department.getCreatedBy());

        // Set createdDate and updatedDate to the current date/time
        // department.setCreatedDate(LocalDateTime.now());
        // department.setUpdatedDate(department.getCreatedDate());

        departmentRepository.persist(department);
        return department;
    }

    @Transactional
    public Department updateDepartment(String id, Department department) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty.");
        }

        if (department == null) {
            throw new IllegalArgumentException("Department cannot be null.");
        }

        // Validate the department object here if needed

        // Get the existing department
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " not found."));
        
        existingDepartment.setDeptName(department.getDeptName());
        existingDepartment.setActive(department.isActive());
        existingDepartment.setUpdatedBy(department.getUpdatedBy());
        existingDepartment.setUpdatedAt(LocalDateTime.now());
        departmentRepository.persist(existingDepartment);

        return existingDepartment;
    }

    @Transactional
    public boolean inactivateDepartment(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty.");
        }

        // Get the existing department
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " not found."));
        
        existingDepartment.setActive(false);
        existingDepartment.setUpdatedBy(existingDepartment.getUpdatedBy());
        existingDepartment.setUpdatedAt(LocalDateTime.now());
        departmentRepository.persist(existingDepartment);

        return true;
    }

    @Transactional
    public boolean activateDepartment(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Department ID cannot be null or empty.");
        }

        // Get the existing department
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with ID " + id + " not found."));
        
        existingDepartment.setActive(true);
        existingDepartment.setUpdatedBy(existingDepartment.getUpdatedBy());
        existingDepartment.setUpdatedAt(LocalDateTime.now());
        departmentRepository.persist(existingDepartment);

        return true;
    }
}
