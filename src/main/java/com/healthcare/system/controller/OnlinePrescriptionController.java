package com.healthcare.system.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.OnlinePrescriptionAnswerDTO;
import com.healthcare.system.dtos.OnlinePrescriptionRequestDTO;
import com.healthcare.system.service.OnlinePrescriptionService;

/**
 * Endpoints for online prescription extension workflow.
 */
@RestController
@RequestMapping("/online-prescriptions")
public class OnlinePrescriptionController {

	private final OnlinePrescriptionService onlinePrescriptionService;

	public OnlinePrescriptionController(OnlinePrescriptionService onlinePrescriptionService) {
		this.onlinePrescriptionService = onlinePrescriptionService;
	}

	@PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
	@PostMapping("/request/{appointmentId}")
	public ResponseEntity<OnlinePrescriptionRequestDTO> requestOnlinePrescription(
			@PathVariable UUID appointmentId,
			@RequestParam UUID patientId,
			@RequestParam String questions) {
		return ResponseEntity.ok(onlinePrescriptionService.createRequest(appointmentId, patientId, questions));
	}

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@PatchMapping("/{requestId}/respond")
	public ResponseEntity<OnlinePrescriptionRequestDTO> respondToRequest(
			@PathVariable UUID requestId,
			@RequestParam UUID doctorId,
			@RequestBody OnlinePrescriptionAnswerDTO answerDTO) {
		return ResponseEntity.ok(onlinePrescriptionService.answerRequest(requestId, doctorId, answerDTO));
	}

	@PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<OnlinePrescriptionRequestDTO>> getDoctorRequests(@PathVariable UUID doctorId) {
		return ResponseEntity.ok(onlinePrescriptionService.getDoctorRequests(doctorId));
	}

	@PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<OnlinePrescriptionRequestDTO>> getPatientRequests(@PathVariable UUID patientId) {
		return ResponseEntity.ok(onlinePrescriptionService.getPatientRequests(patientId));
	}

	@PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN')")
	@GetMapping("/{requestId}/pdf")
	public ResponseEntity<byte[]> downloadPrescriptionPdf(@PathVariable UUID requestId, @RequestParam UUID patientId) {
		byte[] pdfData = onlinePrescriptionService.downloadPrescriptionPdf(requestId, patientId);
		return ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=online-prescription-" + requestId + ".pdf")
				.body(pdfData);
	}
}
