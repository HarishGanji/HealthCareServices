package com.healthcare.system.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;

/**
 * Repository for appointment persistence and query helpers.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

	boolean existsByDoctorAndAppointmentDateTime(Doctor doctor, LocalDateTime appointmentDateTime);

	boolean existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(Doctor doctor, LocalDateTime appointmentDateTime,
			UUID appointmentId);

	List<Appointment> findByPatientPatientId(UUID patientId);

	List<Appointment> findByDoctorDoctorId(UUID doctorId);

//	@Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentDateTime = :appointmentDateTime")
//	long countAppointments(@Param("doctor") Doctor doctor, @Param("appointmentDateTime") LocalDateTime appointmentDateTime);

	@Query("SELECT a FROM Appointment a WHERE a.doctor.doctorId = :doctorId")
	List<Appointment> viewAllAppointments(@Param("doctorId") UUID doctorId);
}
