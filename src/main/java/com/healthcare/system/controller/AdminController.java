package com.healthcare.system.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminServ;

	@DeleteMapping("/deleteDoc/{doctorId}")
	public ResponseEntity<String> deleteDoc(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(adminServ.deleteDoctorById(doctorId), HttpStatus.OK);
	}
	
}
