package com.healthcare.system.serviceimplementation;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.DepartmentDTO;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Department;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.repository.AdministratorRepository;
import com.healthcare.system.repository.DepartmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.DepartmentService;

import jakarta.transaction.Transactional;

@Service
public class DepartmentServiceImplementation implements DepartmentService {

	@Autowired
	DepartmentRepository depRepo;

	@Autowired
	DoctorRepository docRepo;
	
	@Autowired
	AdministratorRepository adminRepo;

	@Override
	public DepartmentDTO createDepartment(Department department) {
		Department dep = new Department();
		dep.setDepartmentName(department.getDepartmentName());
		return convertToDTO(depRepo.save(dep));
	}

	@Override
	public List<DepartmentDTO> getAllDepartments() {
		return depRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public DepartmentDTO getDepartmentById(UUID departmentId) {
		return convertToDTO(depRepo.getDepartmentById(departmentId));
	}

	@Override
	public DepartmentDTO assignDoctorToDepartment(UUID departmentId, UUID doctorId) {
		Department department = depRepo.getDepartmentById(departmentId);
		Doctor doctor = docRepo.getDoctorById(doctorId);
		if (department == null) {
			throw new RuntimeException("Not Found Department");
		}

		if (doctor == null) {
			throw new RuntimeException("Not Found doctor");
		}
		doctor.setDepartment(department);
		docRepo.save(doctor);
		return convertToDTO(department);
	}

	@Override
	public DepartmentDTO assignAdminToDepartment(UUID departmentId, UUID adminId) {
		Department department = depRepo.getDepartmentById(departmentId);
		Administrator admin = adminRepo.getAdminById(adminId);
		if (department == null) {
			throw new RuntimeException("Not Found Department");
		}
		if (admin == null) {
			throw new RuntimeException("Not Found Admin");
		}
		admin.setDepartment(department);
		adminRepo.save(admin);
		return convertToDTO(department);
	}

	@Override
	public DepartmentDTO assignHeadDoctor(UUID departmentId, UUID doctorId) {
		Department department = depRepo.getDepartmentById(departmentId);
		Doctor doctor = docRepo.getDoctorById(doctorId);
		if (department == null) {
			throw new RuntimeException("Not Found Department");
		}
		if (doctor == null) {
			throw new RuntimeException("Not Found doctor");
		}
		department.setHeadDoctor(doctor);
		return convertToDTO(depRepo.save(department));
	}

	private DepartmentDTO convertToDTO(Department depart) {
		DepartmentDTO depDTO = new DepartmentDTO();
		depDTO.setDepartmentId(depart.getDepartmentId());
		depDTO.setDepartmentName(depart.getDepartmentName());
		return depDTO;
	}

}
