package com.healthcare.system.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.AppointmentDTO;
import com.healthcare.system.enums.Status;

public interface AppointmentService {
	
	AppointmentDTO bookAppointment(UUID patientId, UUID doctorId, LocalDateTime appointmentDateTime);

	List<AppointmentDTO> getAppointmentsByPatientId(UUID patientId);

	List<AppointmentDTO> getAppointmentsByDoctorId(UUID doctorId);

	List<AppointmentDTO> getAllAppointments();

	AppointmentDTO getAppointmentById(UUID appointmentId);

	AppointmentDTO cancelAppointment(UUID appointmentId);

	AppointmentDTO rescheduleAppointment(UUID appointmentId, LocalDateTime appointmentDateTime);

	AppointmentDTO updateAppointmentStatus(UUID appointmentId, Status status);
}
