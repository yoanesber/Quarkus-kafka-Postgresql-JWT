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
        departmentDTO.setCreatedDate(department.getCreatedDate());
        departmentDTO.setUpdatedBy(department.getUpdatedBy());
        departmentDTO.setUpdatedDate(department.getUpdatedDate());
        
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
        department.setCreatedDate(departmentDTO.getCreatedDate());
        department.setUpdatedBy(departmentDTO.getUpdatedBy());
        department.setUpdatedDate(departmentDTO.getUpdatedDate());

        return department;
    }
}
