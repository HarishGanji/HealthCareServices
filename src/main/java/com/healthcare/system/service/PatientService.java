package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Patient;

public interface PatientService {
	
	Patient completeProfile(UUID patientId, Patient patient);
	
	AddressDTO getAddressByPatientId(UUID patientId);

	
}
