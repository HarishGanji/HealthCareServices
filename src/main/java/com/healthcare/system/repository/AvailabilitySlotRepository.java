package com.healthcare.system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthcare.system.models.AvailabilitySlot;
import com.healthcare.system.models.Doctor;

/**
 * Repository for doctor availability slots and overlap queries.
 */
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, UUID> {

	List<AvailabilitySlot> findByDoctorDoctorId(UUID doctorId);

	@Query("""
			SELECT COUNT(s) > 0 FROM AvailabilitySlot s
			WHERE s.doctor = :doctor
			  AND s.endTime > :startTime
			  AND s.startTime < :endTime
			""")
	boolean existsOverlappingSlot(@Param("doctor") Doctor doctor,
			@Param("startTime") LocalDateTime startTime,
			@Param("endTime") LocalDateTime endTime);
}
