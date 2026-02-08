package com.healthcare.system.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AppointmentDTO;
import com.healthcare.system.enums.Status;
import com.healthcare.system.service.AppointmentService;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	AppointmentService appointServ;

	@PostMapping("/appointment/{patientId}/{doctorId}")
	public ResponseEntity<AppointmentDTO> bookAppointment(@PathVariable UUID patientId, @PathVariable UUID doctorId,
			@RequestParam LocalDateTime appointmentDateTime) {
		return new ResponseEntity<>(appointServ.bookAppointment(patientId, doctorId, appointmentDateTime),
				HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<AppointmentDTO>> getAllAppointments() {
		return new ResponseEntity<>(appointServ.getAllAppointments(), HttpStatus.OK);
	}

	@GetMapping("/{appointmentId}")
	public ResponseEntity<AppointmentDTO> getAppointment(@PathVariable UUID appointmentId) {
		return new ResponseEntity<>(appointServ.getAppointmentById(appointmentId), HttpStatus.OK);
	}

	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@PathVariable UUID patientId) {
		return new ResponseEntity<>(appointServ.getAppointmentsByPatientId(patientId), HttpStatus.OK);
	}

	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(appointServ.getAppointmentsByDoctorId(doctorId), HttpStatus.OK);
	}

	@PatchMapping("/{appointmentId}/cancel")
	public ResponseEntity<AppointmentDTO> cancelAppointment(@PathVariable UUID appointmentId) {
		return new ResponseEntity<>(appointServ.cancelAppointment(appointmentId), HttpStatus.OK);
	}

	@PatchMapping("/{appointmentId}/reschedule")
	public ResponseEntity<AppointmentDTO> rescheduleAppointment(@PathVariable UUID appointmentId,
			@RequestParam LocalDateTime appointmentDateTime) {
		return new ResponseEntity<>(appointServ.rescheduleAppointment(appointmentId, appointmentDateTime),
				HttpStatus.OK);
	}

	@PatchMapping("/{appointmentId}/status")
	public ResponseEntity<AppointmentDTO> updateAppointmentStatus(@PathVariable UUID appointmentId,
			@RequestParam Status status) {
		return new ResponseEntity<>(appointServ.updateAppointmentStatus(appointmentId, status), HttpStatus.OK);
	}
}
