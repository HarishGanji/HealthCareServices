package com.healthcare.system.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.PrescriptionDTO;
import com.healthcare.system.service.PrescriptionService;

/**
 * Prescription endpoints for doctors and patients.
 */
@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

	@Autowired
	private PrescriptionService prescriptionService;

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PostMapping("/doctor/{doctorId}/patient/{patientId}")
	public ResponseEntity<PrescriptionDTO> createPrescription(@PathVariable UUID doctorId,
			@PathVariable UUID patientId,
			@RequestBody PrescriptionDTO prescription) {
		return new ResponseEntity<>(prescriptionService.createPrescription(doctorId, patientId, prescription),
				HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<PrescriptionDTO>> getByPatient(@PathVariable UUID patientId) {
		return new ResponseEntity<>(prescriptionService.getPrescriptionsByPatient(patientId), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<PrescriptionDTO>> getByDoctor(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(prescriptionService.getPrescriptionsByDoctor(doctorId), HttpStatus.OK);
	}
}
