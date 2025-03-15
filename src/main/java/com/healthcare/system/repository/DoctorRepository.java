package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

	@Query("SELECT d FROM Doctor d WHERE d.doctorId =:doctorId")
	Doctor getDoctorById(@Param("doctorId") UUID doctorId);
}
