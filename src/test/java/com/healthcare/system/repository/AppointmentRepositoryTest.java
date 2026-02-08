package com.healthcare.system.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.healthcare.system.enums.Role;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;

@DataJpaTest
class AppointmentRepositoryTest {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	void appointmentQueriesReturnExpectedResults() {
		User doctorUser = new User();
		doctorUser.setUsername("doctor");
		doctorUser.setEmail("doc@example.com");
		doctorUser.setPassword("password");
		doctorUser.setRole(Role.DOCTOR);

		User patientUser = new User();
		patientUser.setUsername("patient");
		patientUser.setEmail("patient@example.com");
		patientUser.setPassword("password");
		patientUser.setRole(Role.PATIENT);

		Doctor doctor = new Doctor();
		doctor.setDoctorName("Dr. Test");
		doctor.setSpecialization("cardiology");
		doctor.setEmail("doc@example.com");
		doctor.setContactNumber("555-0001");
		doctor.setUser(doctorUser);

		Patient patient = new Patient();
		patient.setPatientName("Patient One");
		patient.setEmail("patient@example.com");
		patient.setContactNumber("555-0002");
		patient.setUser(patientUser);

		entityManager.persist(doctorUser);
		entityManager.persist(patientUser);
		entityManager.persist(doctor);
		entityManager.persist(patient);

		LocalDateTime appointmentTime = LocalDateTime.of(2025, 1, 15, 14, 0);
		Appointment appointment = new Appointment();
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setAppointmentDateTime(appointmentTime);
		Appointment saved = entityManager.persistAndFlush(appointment);

		assertThat(appointmentRepository.existsByDoctorAndAppointmentDateTime(doctor, appointmentTime)).isTrue();
		assertThat(appointmentRepository.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(doctor, appointmentTime,
				saved.getAppointmentId())).isFalse();

		List<Appointment> byPatient = appointmentRepository.findByPatientPatientId(patient.getPatientId());
		List<Appointment> byDoctor = appointmentRepository.findByDoctorDoctorId(doctor.getDoctorId());

		assertThat(byPatient).hasSize(1);
		assertThat(byDoctor).hasSize(1);
		assertThat(byDoctor.get(0).getAppointmentId()).isEqualTo(saved.getAppointmentId());
	}
}
