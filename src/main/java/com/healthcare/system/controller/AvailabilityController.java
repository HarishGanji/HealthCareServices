package com.healthcare.system.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AvailabilitySlotDTO;
import com.healthcare.system.service.AvailabilityService;

/**
 * Availability management endpoints for doctor scheduling.
 */
@RestController
@RequestMapping("/availability")
public class AvailabilityController {

	@Autowired
	private AvailabilityService availabilityService;

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PostMapping("/doctor/{doctorId}")
	public ResponseEntity<AvailabilitySlotDTO> createAvailability(@PathVariable UUID doctorId,
			@RequestParam LocalDateTime startTime,
			@RequestParam LocalDateTime endTime) {
		return new ResponseEntity<>(
				availabilityService.createAvailabilitySlot(doctorId, startTime, endTime), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('PATIENT', 'DOCTOR', 'ADMIN')")
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<AvailabilitySlotDTO>> getAvailability(@PathVariable UUID doctorId) {
		return new ResponseEntity<>(availabilityService.getAvailabilityByDoctor(doctorId), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PatchMapping("/{slotId}")
	public ResponseEntity<AvailabilitySlotDTO> updateAvailability(@PathVariable UUID slotId,
			@RequestParam boolean available) {
		return new ResponseEntity<>(availabilityService.updateAvailability(slotId, available), HttpStatus.OK);
	}
}
