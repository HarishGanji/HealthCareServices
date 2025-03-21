package com.healthcare.system.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AppointmentDTO;
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

}
