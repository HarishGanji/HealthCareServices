package com.healthcare.system.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePrescriptionAnswerDTO {
	private String doctorResponse;
	private String medicationName;
	private String dosage;
	private String instructions;
}
