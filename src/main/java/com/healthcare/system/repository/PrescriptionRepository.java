package com.healthcare.system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthcare.system.models.Prescription;

/**
 * Repository for prescription entities.
 */
public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

	List<Prescription> findByPatientPatientId(UUID patientId);

	List<Prescription> findByDoctorDoctorId(UUID doctorId);
}
