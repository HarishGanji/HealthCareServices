package com.healthcare.system.service;

import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.DepartmentDTO;
import com.healthcare.system.models.Department;

/**
 * Service contract for department creation and assignments.
 */
public interface DepartmentService {

	DepartmentDTO createDepartment(Department department);

	List<DepartmentDTO> getAllDepartments();

	DepartmentDTO getDepartmentById(UUID departmentId);

	DepartmentDTO assignDoctorToDepartment(UUID departmentId, UUID doctorId);

	DepartmentDTO assignHeadDoctor(UUID departmentId, UUID doctorId);

	DepartmentDTO assignAdminToDepartment(UUID departmentId, UUID adminId);
}
