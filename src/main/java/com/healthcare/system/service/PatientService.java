package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.models.Patient;

public interface PatientService {
	
	Patient completeProfile(UUID patientId, Patient patient, String token);
	
	
}
