package com.healthcare.system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Doctor;

/**
 * Repository for doctor entities and query helpers.
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

	@Query("SELECT d FROM Doctor d WHERE d.doctorId =:doctorId")
	Doctor getDoctorById(@Param("doctorId") UUID doctorId);

	@Modifying
	@Query("DELETE FROM Doctor d WHERE d.doctorId = :doctorId")
	int deleteDoctorById(@Param("doctorId") UUID doctorId);

	@Query(value = "SELECT doctor_id FROM doctor WHERE user_id = :userId", nativeQuery = true)
	UUID getDoctorByUserId(@Param("userId") UUID userId);

	@Query("SELECT d FROM Doctor d WHERE d.specialization =:specialization")
	List<Doctor> doctorsBySpecialization(String specialization);
}
