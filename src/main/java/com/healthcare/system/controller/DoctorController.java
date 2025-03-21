package com.healthcare.system.controller;

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

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.service.DoctorService;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	DoctorService docterServ;

	@PostMapping("/specialization/{doctorId}")
	public ResponseEntity<Doctor> postSpecialization(@PathVariable UUID doctorId, @RequestBody Doctor doctor) {
		return new ResponseEntity<>(docterServ.addOrEditSpecialization(doctorId, doctor), HttpStatus.OK);
	}

	@PostMapping("/complete-profile/{doctorId}")
	public ResponseEntity<Doctor> completeProfile(@PathVariable UUID doctorId, @RequestBody Doctor doc) {
		return new ResponseEntity<>(docterServ.completeProfile(doctorId, doc), HttpStatus.OK);
	}
	
	@GetMapping("/address/{doctorId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(docterServ.getAddressByDoctorId(doctorId), HttpStatus.OK);
	}
}
