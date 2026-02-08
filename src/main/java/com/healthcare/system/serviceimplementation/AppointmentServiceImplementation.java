package com.healthcare.system.serviceimplementation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AppointmentDTO;
import com.healthcare.system.enums.Status;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.service.AppointmentService;

@Service
public class AppointmentServiceImplementation implements AppointmentService{
	
	@Autowired
	AppointmentRepository appointRepo;
	
	@Autowired
	DoctorRepository doctorRepo;
	
	@Autowired
	PatientRepository patientRepo;
	
	@Override
	public AppointmentDTO bookAppointment(UUID patientId, UUID doctorId, LocalDateTime appointmentDateTime) {
		Patient patient = patientRepo.getPatientById(patientId);
		Doctor doctor = doctorRepo.getDoctorById(doctorId);
		
		if(patient == null) {
			throw new RuntimeException("Patient Not Found");
		}
		if(doctor == null) {
			throw new RuntimeException("Doctor not Found");
		}
		
		boolean isDoctorAvailable = appointRepo.existsByDoctorAndAppointmentDateTime(doctor, appointmentDateTime);
		if(isDoctorAvailable) {
			throw new RuntimeException("Doctor Not Available");
		}
		
		Appointment app = new Appointment();
		app.setCreatedAt(LocalDateTime.now());
		app.setDoctor(doctor);
		app.setPatient(patient);
		app.setStatus(Status.PENDING);
		app.setUpdatedAt(LocalDateTime.now());
		app.setAppointmentDateTime(appointmentDateTime);
		return convertToDTO(appointRepo.save(app));
	}

	@Override
	public List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId) {
		Patient patient = patientRepo.getPatientById(patientId);
		if (patient == null) {
			throw new RuntimeException("Patient Not Found");
		}
		return appointRepo.findByPatientPatientId(patientId).stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<AppointmentDTO> getAppointmentsByDoctorId(UUID doctorId) {
		Doctor doctor = doctorRepo.getDoctorById(doctorId);
		if (doctor == null) {
			throw new RuntimeException("Doctor not Found");
		}
		return appointRepo.findByDoctorDoctorId(doctorId).stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public List<AppointmentDTO> getAllAppointments() {
		return appointRepo.findAll().stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	@Override
	public AppointmentDTO getAppointmentById(UUID appointmentId) {
		Appointment appointment = appointRepo.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment Not Found"));
		return convertToDTO(appointment);
	}

	@Override
	public AppointmentDTO cancelAppointment(UUID appointmentId) {
		Appointment appointment = appointRepo.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment Not Found"));
		appointment.setStatus(Status.CANCELLED);
		appointment.setUpdatedAt(LocalDateTime.now());
		return convertToDTO(appointRepo.save(appointment));
	}

	@Override
	public AppointmentDTO rescheduleAppointment(UUID appointmentId, LocalDateTime appointmentDateTime) {
		Appointment appointment = appointRepo.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment Not Found"));
		Doctor doctor = appointment.getDoctor();
		boolean isDoctorAvailable = appointRepo.existsByDoctorAndAppointmentDateTimeAndAppointmentIdNot(
				doctor, appointmentDateTime, appointmentId);
		if (isDoctorAvailable) {
			throw new RuntimeException("Doctor Not Available");
		}
		appointment.setAppointmentDateTime(appointmentDateTime);
		appointment.setStatus(Status.RESCHEDULED);
		appointment.setUpdatedAt(LocalDateTime.now());
		return convertToDTO(appointRepo.save(appointment));
	}

	@Override
	public AppointmentDTO updateAppointmentStatus(UUID appointmentId, Status status) {
		Appointment appointment = appointRepo.findById(appointmentId)
				.orElseThrow(() -> new RuntimeException("Appointment Not Found"));
		appointment.setStatus(status);
		appointment.setUpdatedAt(LocalDateTime.now());
		return convertToDTO(appointRepo.save(appointment));
	}

	private AppointmentDTO convertToDTO(Appointment app) {
		AppointmentDTO appoint = new AppointmentDTO();
		appoint.setAppointmentId(app.getAppointmentId());
		appoint.setPatient_Id(app.getPatient().getPatientId());
		appoint.setDoctor_Id(app.getDoctor().getDoctorId());
		appoint.setCreatedAt(app.getCreatedAt());
		appoint.setStatus(app.getStatus());
		appoint.setAppointmentDateTime(app.getAppointmentDateTime());
		appoint.setUpdatedAt(app.getUpdatedAt());
		return appoint;
	}

}
