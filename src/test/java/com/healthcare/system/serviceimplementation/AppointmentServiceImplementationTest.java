package com.healthcare.system.serviceimplementation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
	void bookAppointmentCreatesPendingAppointment() {
		UUID patientId = UUID.randomUUID();
		UUID doctorId = UUID.randomUUID();
		LocalDateTime appointmentTime = LocalDateTime.of(2025, 1, 1, 9, 0);
		Patient patient = new Patient();
		patient.setPatientId(patientId);
		Doctor doctor = new Doctor();
		doctor.setDoctorId(doctorId);

		when(patientRepository.getPatientById(patientId)).thenReturn(patient);
		when(doctorRepository.getDoctorById(doctorId)).thenReturn(doctor);
		when(appointmentRepository.existsByDoctorAndAppointmentDateTime(eq(doctor), eq(appointmentTime))).thenReturn(false);
		when(appointmentRepository.save(any(Appointment.class))).thenAnswer(invocation -> {
			Appointment saved = invocation.getArgument(0);
			saved.setAppointmentId(UUID.randomUUID());
			return saved;
		});

		AppointmentDTO result = appointmentService.bookAppointment(patientId, doctorId, appointmentTime);

		assertThat(result.getStatus()).isEqualTo(Status.PENDING);
		assertThat(result.getAppointmentDateTime()).isEqualTo(appointmentTime);
		assertThat(result.getDoctor_Id()).isEqualTo(doctorId);
		assertThat(result.getPatient_Id()).isEqualTo(patientId);
	}

	@Test
	void rescheduleAppointmentThrowsWhenDoctorUnavailable() {
		UUID appointmentId = UUID.randomUUID();
		Doctor doctor = new Doctor();
		doctor.setDoctorId(UUID.randomUUID());
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setDoctor(doctor);

		when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(eq(doctor), any(),
				eq(appointmentId))).thenReturn(true);

		assertThatThrownBy(() -> appointmentService.rescheduleAppointment(appointmentId, LocalDateTime.now()))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("Doctor Not Available");
	}

	@Test
	void cancelAppointmentUpdatesStatus() {
		UUID appointmentId = UUID.randomUUID();
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(appointmentId);
		appointment.setStatus(Status.PENDING);

		when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(appointment)).thenReturn(appointment);

		AppointmentDTO result = appointmentService.cancelAppointment(appointmentId);

		assertThat(result.getStatus()).isEqualTo(Status.CANCELLED);
	}

	@Test
	void getAppointmentsByPatientReturnsDtos() {
		UUID patientId = UUID.randomUUID();
		Patient patient = new Patient();
		patient.setPatientId(patientId);
		Doctor doctor = new Doctor();
		doctor.setDoctorId(UUID.randomUUID());
		Appointment appointment = new Appointment();
		appointment.setAppointmentId(UUID.randomUUID());
		appointment.setPatient(patient);
		appointment.setDoctor(doctor);
		appointment.setAppointmentDateTime(LocalDateTime.now());
		appointment.setStatus(Status.PENDING);

		when(patientRepository.getPatientById(patientId)).thenReturn(patient);
		when(appointmentRepository.findByPatientPatientId(patientId)).thenReturn(List.of(appointment));

		List<AppointmentDTO> result = appointmentService.getAppointmentsByPatientId(patientId);

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getPatient_Id()).isEqualTo(patientId);
	}
}
