package com.healthcare.system.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.models.Patient;
import com.healthcare.system.service.PatientService;

@RestController
@RequestMapping("/patient")
public class PatientController {

	@Autowired
	PatientService patientServ;

	@PostMapping("/profile/{patientId}")
	public ResponseEntity<Patient> completeProfile(@PathVariable UUID patientId, @RequestBody Patient pat) {
		return new ResponseEntity<>(patientServ.completeProfile(patientId, pat), HttpStatus.OK);
	}

}
