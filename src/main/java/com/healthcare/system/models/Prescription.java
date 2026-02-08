package com.healthcare.system.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Prescription entity created by a doctor for a patient with medication details.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prescription {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID prescriptionId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", nullable = false)
	@JsonIgnore
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	@JsonIgnore
	private Patient patient;

	private String medicationName;

	private String dosage;

	private String instructions;

	private LocalDateTime issuedAt;

	@PrePersist
	void onPersist() {
		if (issuedAt == null) {
			issuedAt = LocalDateTime.now();
		}
	}
}
