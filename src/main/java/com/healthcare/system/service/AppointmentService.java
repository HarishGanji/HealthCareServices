package com.healthcare.system.service;

import java.time.LocalDateTime;
import java.util.UUID;

import com.healthcare.system.dtos.AppointmentDTO;

public interface AppointmentService {
	
	AppointmentDTO bookAppointment(UUID patientId, UUID doctorId, LocalDateTime appointmentDateTime);
}
