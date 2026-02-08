package com.healthcare.system.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.healthcare.system.dtos.AppointmentDTO;
import com.healthcare.system.enums.Status;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.serviceimplementation.AppointmentServiceImplementation;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplementationTest {

	@Mock
	private AppointmentRepository appointmentRepository;

	@Mock
	private DoctorRepository doctorRepository;

	@Mock
	private PatientRepository patientRepository;

	@InjectMocks
	private AppointmentServiceImplementation appointmentService;

	@Test
	void bookAppointment_createsPendingAppointment() {
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		LocalDateTime appointmentTime = LocalDateTime.of(2025, 1, 1, 10, 0);
		Patient patient = new Patient();
		patient.setPatientId(patientId);
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorId);
		Appointment savedAppointment = new Appointment();
		savedAppointment.setAppointmentId(UUID.randomUUID());
		savedAppointment.setPatient(patient);
		savedAppointment.setDoctor(doctor);
		savedAppointment.setAppointmentDateTime(appointmentTime);
		savedAppointment.setStatus(Status.PENDING);

		when(patientRepository.getPatientById(patientId)).thenReturn(patient);
		when(doctorRepository.getDoctorById(doctorId)).thenReturn(doctor);
		when(appointmentRepository.existsByDoctorAndAppointmentDateTime(doctor, appointmentTime)).thenReturn(false);
		when(appointmentRepository.save(org.mockito.ArgumentMatchers.any(Appointment.class))).thenReturn(savedAppointment);

		AppointmentDTO result = appointmentService.bookAppointment(patientId, doctorId, appointmentTime);

		assertThat(result.getPatient_Id()).isEqualTo(patientId);
		assertThat(result.getDoctor_Id()).isEqualTo(doctorId);
		assertThat(result.getStatus()).isEqualTo(Status.PENDING);
	}

	@Test
	void getAppointmentsByPatientId_throwsWhenMissingPatient() {
		UUID patientId = UUID.randomUUID();
		when(patientRepository.getPatientById(patientId)).thenReturn(null);

		assertThatThrownBy(() -> appointmentService.getAppointmentsByPatientId(patientId))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Patient Not Found");
	}

	@Test
	void getAllAppointments_returnsDtos() {
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(UUID.randomUUID());
		appointment.setPatient(new Patient());
		appointment.setDoctor(new Doctor());

		when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

		List<AppointmentDTO> results = appointmentService.getAllAppointments();

		assertThat(results).hasSize(1);
	}

	@Test
	void cancelAppointment_setsCancelledStatus() {
		UUID appointmentId = UUID.randomUUID();
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setPatient(new Patient());
		appointment.setDoctor(new Doctor());

		when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(appointment)).thenReturn(appointment);

		AppointmentDTO result = appointmentService.cancelAppointment(appointmentId);

		assertThat(result.getStatus()).isEqualTo(Status.CANCELLED);
		verify(appointmentRepository).save(appointment);
	}

	@Test
	void rescheduleAppointment_checksAvailability() {
		UUID appointmentId = UUID.randomUUID();
		LocalDateTime newTime = LocalDateTime.of(2025, 1, 2, 12, 0);
		Doctor doctor = new Doctor();
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setDoctor(doctor);
		appointment.setPatient(new Patient());

		when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(doctor, newTime,
				appointmentId)).thenReturn(false);
		when(appointmentRepository.save(appointment)).thenReturn(appointment);

		AppointmentDTO result = appointmentService.rescheduleAppointment(appointmentId, newTime);

		assertThat(result.getStatus()).isEqualTo(Status.RESCHEDULED);
		assertThat(result.getAppointmentDateTime()).isEqualTo(newTime);
	}

	@Test
	void updateAppointmentStatus_setsStatus() {
		UUID appointmentId = UUID.randomUUID();
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setPatient(new Patient());
		appointment.setDoctor(new Doctor());

		when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(appointment)).thenReturn(appointment);

		AppointmentDTO result = appointmentService.updateAppointmentStatus(appointmentId, Status.ACCEPTED);

		assertThat(result.getStatus()).isEqualTo(Status.ACCEPTED);
	}
}
