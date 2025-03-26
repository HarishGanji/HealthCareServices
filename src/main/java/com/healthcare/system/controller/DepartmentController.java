package com.healthcare.system.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.DepartmentDTO;
import com.healthcare.system.models.Department;
import com.healthcare.system.service.DepartmentService;

@RestController
@RequestMapping("/auth")
public class DepartmentController {

	@Autowired
	DepartmentService depServ;

	@PostMapping("/department")
	public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody Department department) {
		return new ResponseEntity<>(depServ.createDepartment(department), HttpStatus.OK);
	}

	@GetMapping("/viewdepartments")
	public ResponseEntity<List<DepartmentDTO>> viewAllDepartments() {
		return new ResponseEntity<>(depServ.getAllDepartments(), HttpStatus.OK);
	}

	@GetMapping("/viewdepartment/{departmentId}")
	public ResponseEntity<DepartmentDTO> viewDepartmentById(@PathVariable UUID departmentId) {
		return new ResponseEntity<>(depServ.getDepartmentById(departmentId), HttpStatus.OK);
	}

	@PostMapping("/assigndoctor/{departmentId}/{doctorId}")
	public ResponseEntity<DepartmentDTO> assignDoctorToDepartment(@PathVariable UUID departmentId,
			@PathVariable UUID doctorId) {
		return new ResponseEntity<>(depServ.assignDoctorToDepartment(departmentId, doctorId), HttpStatus.OK);
	}

	@PostMapping("/assign-admin/{departmentId}/{adminId}")
	public ResponseEntity<DepartmentDTO> assignAdminToDepartment(@PathVariable UUID departmentId,
			@PathVariable UUID adminId) {
		return new ResponseEntity<>(depServ.assignAdminToDepartment(departmentId, adminId), HttpStatus.OK);
	}

	@PostMapping("/head-doctor/{departmentId}/{doctorId}")
	public ResponseEntity<DepartmentDTO> headDoctor(@PathVariable UUID departmentId, @PathVariable UUID doctorId) {
		return new ResponseEntity<>(depServ.assignHeadDoctor(departmentId, doctorId), HttpStatus.OK);
	}
}
