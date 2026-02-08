package com.healthcare.system.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.service.AdminService;

/**
 * Administrative endpoints for managing doctors and admin profiles.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminService adminServ;

	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/deleteDoc/{doctorId}")
	public ResponseEntity<String> deleteDoc(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(adminServ.deleteDoctorById(doctorId), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/complete-profile/{adminId}")
	public ResponseEntity<Administrator> completeProfile(@PathVariable UUID adminId, @RequestBody Administrator admin) {
		return new ResponseEntity<>(adminServ.completeProfile(adminId, admin), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/address/{adminId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable UUID adminId) {
		return new ResponseEntity<>(adminServ.getAddressByAdminId(adminId), HttpStatus.OK);
	}
}
