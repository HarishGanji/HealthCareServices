package com.healthcare.system.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

	private UUID prescriptionId;
	private UUID doctorId;
	private UUID patientId;
	private String medicationName;
	private String dosage;
	private String instructions;
	private LocalDateTime issuedAt;
}
