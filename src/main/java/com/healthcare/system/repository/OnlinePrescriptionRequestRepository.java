package com.healthcare.system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.system.models.OnlinePrescriptionRequest;

public interface OnlinePrescriptionRequestRepository extends JpaRepository<OnlinePrescriptionRequest, UUID> {
	List<OnlinePrescriptionRequest> findByDoctorDoctorId(UUID doctorId);
	List<OnlinePrescriptionRequest> findByPatientPatientId(UUID patientId);
}
