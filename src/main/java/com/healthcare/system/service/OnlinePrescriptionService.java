package com.healthcare.system.service;

import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.OnlinePrescriptionAnswerDTO;
import com.healthcare.system.dtos.OnlinePrescriptionRequestDTO;

public interface OnlinePrescriptionService {
	OnlinePrescriptionRequestDTO createRequest(UUID appointmentId, UUID patientId, String questions);
	OnlinePrescriptionRequestDTO answerRequest(UUID requestId, UUID doctorId, OnlinePrescriptionAnswerDTO answerDTO);
	List<OnlinePrescriptionRequestDTO> getDoctorRequests(UUID doctorId);
	List<OnlinePrescriptionRequestDTO> getPatientRequests(UUID patientId);
	byte[] downloadPrescriptionPdf(UUID requestId, UUID patientId);
}
