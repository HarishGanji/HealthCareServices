package com.healthcare.system.serviceimplementation;

import java.time.LocalDateTime;
import java.util.UUID;

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
