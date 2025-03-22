package com.healthcare.system.service;

import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;

public interface DoctorService {

	Doctor addOrEditSpecialization(UUID doctorId, Doctor doctor);

	AddressDTO getAddressByDoctorId(UUID doctorId);

	Doctor completeProfile(UUID doctorId, Doctor doctor);

	List<Doctor> getDoctorsBySpecialization(String specialization);
	
	List<Appointment> viewAllAppointmentsByDoctorId(UUID doctorId);
}
