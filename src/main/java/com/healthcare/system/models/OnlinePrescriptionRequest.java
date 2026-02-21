package com.healthcare.system.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.healthcare.system.enums.OnlinePrescriptionRequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request raised by patient for online prescription extension after first completed appointment.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePrescriptionRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID requestId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	@JsonIgnore
	private Patient patient;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", nullable = false)
	@JsonIgnore
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", nullable = false)
	@JsonIgnore
	private Appointment appointment;

	private String patientQuestions;

	private String doctorResponse;

	@Enumerated(EnumType.STRING)
	private OnlinePrescriptionRequestStatus status;

	private LocalDateTime requestedAt;

	private LocalDateTime respondedAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prescription_id")
	@JsonIgnore
	private Prescription prescription;
}
