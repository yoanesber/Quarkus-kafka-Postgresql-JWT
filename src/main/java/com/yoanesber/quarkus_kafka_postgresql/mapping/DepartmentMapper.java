package com.yoanesber.quarkus_kafka_postgresql.mapping;

import jakarta.enterprise.context.ApplicationScoped;

import com.yoanesber.quarkus_kafka_postgresql.dto.DepartmentDTO;
import com.yoanesber.quarkus_kafka_postgresql.entity.Department;

@ApplicationScoped
public class DepartmentMapper {
    public DepartmentDTO toDTO(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setDeptName(department.getDeptName());
        departmentDTO.setActive(department.isActive());
        departmentDTO.setCreatedBy(department.getCreatedBy());
        departmentDTO.setCreatedAt(department.getCreatedAt());
        departmentDTO.setUpdatedBy(department.getUpdatedBy());
        departmentDTO.setUpdatedAt(department.getUpdatedAt());
        departmentDTO.setDeletedBy(department.getDeletedBy());
        departmentDTO.setDeletedAt(department.getDeletedAt());
        
        return departmentDTO;
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        if (departmentDTO == null) {
            return null;
        }

        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setDeptName(departmentDTO.getDeptName());
        department.setActive(departmentDTO.isActive());
        department.setCreatedBy(departmentDTO.getCreatedBy());
        department.setCreatedAt(departmentDTO.getCreatedAt());
        department.setUpdatedBy(departmentDTO.getUpdatedBy());
        department.setUpdatedAt(departmentDTO.getUpdatedAt());
        department.setDeletedBy(departmentDTO.getDeletedBy());
        department.setDeletedAt(departmentDTO.getDeletedAt());

        return department;
    }
}
