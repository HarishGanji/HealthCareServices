package com.healthcare.system.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.healthcare.system.enums.OnlinePrescriptionRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePrescriptionRequestDTO {
	private UUID requestId;
	private UUID patientId;
	private UUID doctorId;
	private UUID appointmentId;
	private String patientQuestions;
	private String doctorResponse;
	private OnlinePrescriptionRequestStatus status;
	private LocalDateTime requestedAt;
	private LocalDateTime respondedAt;
	private UUID prescriptionId;
}
