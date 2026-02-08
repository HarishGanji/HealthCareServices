package com.healthcare.system.service;

import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.PrescriptionDTO;

/**
 * Service contract for creating and retrieving prescriptions.
 */
public interface PrescriptionService {

	PrescriptionDTO createPrescription(UUID doctorId, UUID patientId, PrescriptionDTO prescription);

	List<PrescriptionDTO> getPrescriptionsByPatient(UUID patientId);

	List<PrescriptionDTO> getPrescriptionsByDoctor(UUID doctorId);
}
