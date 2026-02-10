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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.service.DoctorService;

/**
 * Doctor profile and lookup endpoints.
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

	@Autowired
	DoctorService doctorServ;

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PostMapping("/specialization/{doctorId}")
	public ResponseEntity<Doctor> postSpecialization(@PathVariable UUID doctorId, @RequestBody Doctor doctor) {
		return new ResponseEntity<>(doctorServ.addOrEditSpecialization(doctorId, doctor), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PostMapping("/complete-profile/{doctorId}")
	public ResponseEntity<Doctor> completeProfile(@PathVariable UUID doctorId, @RequestBody Doctor doc) {
		return new ResponseEntity<>(doctorServ.completeProfile(doctorId, doc), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@GetMapping("/address/{doctorId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(doctorServ.getAddressByDoctorId(doctorId), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('PATIENT', 'DOCTOR', 'ADMIN')")
	@GetMapping("/doctors/specialization")
	public ResponseEntity<List<Doctor>> getDoctorsBySpecialization(@RequestParam String specialization){
		return new ResponseEntity<>(doctorServ.getDoctorsBySpecialization(specialization),HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@GetMapping("/appointments/{doctorId}")
	public ResponseEntity<List<Appointment>> viewAllAppointments(@PathVariable UUID doctorId){
		return new ResponseEntity<>(doctorServ.viewAllAppointmentsByDoctorId(doctorId),HttpStatus.OK);
	}
}
