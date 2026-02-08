package com.healthcare.system.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
	private DoctorRepository doctorRepository;

	@Autowired
	private PatientRepository patientRepository;

	@Test
	void findByDoctorAndPatientIds_returnsMatchingAppointments() {
		Doctor doctor = new Doctor();
		doctor.setDoctorName("Dr. Smith");
		doctor.setUser(buildUser(Role.DOCTOR, "doctor1"));
		doctor = doctorRepository.save(doctor);

		Patient patient = new Patient();
		patient.setPatientName("Jane Doe");
		patient.setUser(buildUser(Role.PATIENT, "patient1"));
		patient = patientRepository.save(patient);

		LocalDateTime time = LocalDateTime.of(2025, 1, 5, 9, 0);
		Appointment appointment = new Appointment();
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setAppointmentDateTime(time);
		appointmentRepository.save(appointment);

		List<Appointment> doctorAppointments = appointmentRepository.findByDoctorDoctorId(doctor.getDoctorId());
		List<Appointment> patientAppointments = appointmentRepository.findByPatientPatientId(patient.getPatientId());

		assertThat(doctorAppointments).hasSize(1);
		assertThat(patientAppointments).hasSize(1);
	}

	@Test
	void existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot_respectsExclusion() {
		Doctor doctor = new Doctor();
		doctor.setDoctorName("Dr. Watson");
		doctor.setUser(buildUser(Role.DOCTOR, "doctor2"));
		doctor = doctorRepository.save(doctor);

		Patient patient = new Patient();
		patient.setPatientName("John Doe");
		patient.setUser(buildUser(Role.PATIENT, "patient2"));
		patient = patientRepository.save(patient);

		LocalDateTime time = LocalDateTime.of(2025, 1, 6, 14, 0);
		Appointment appointment = new Appointment();
		appointment.setDoctor(doctor);
		appointment.setPatient(patient);
		appointment.setAppointmentDateTime(time);
		appointment = appointmentRepository.save(appointment);

		boolean existsExcludingSame = appointmentRepository.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(
				doctor, time, appointment.getAppointmentId());
		assertThat(existsExcludingSame).isFalse();

		Appointment otherAppointment = new Appointment();
		otherAppointment.setDoctor(doctor);
		otherAppointment.setPatient(patient);
		otherAppointment.setAppointmentDateTime(time);
		appointmentRepository.save(otherAppointment);

		boolean existsExcludingOther = appointmentRepository.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(
				doctor, time, appointment.getAppointmentId());
		assertThat(existsExcludingOther).isTrue();
	}

	private User buildUser(Role role, String username) {
		User user = new User();
		user.setUsername(username);
		user.setEmail(username + "@example.com");
		user.setPassword("password");
		user.setPhoneNumber("1234567890");
		user.setRole(role);
		return user;
	}
}
