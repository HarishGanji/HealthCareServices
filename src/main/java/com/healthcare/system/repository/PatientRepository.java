package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

	@Query("SELECT p FROM Patient p WHERE p.patientId =:patientId")
	Patient getPatientById(@Param("patientId") UUID patientId);
}
