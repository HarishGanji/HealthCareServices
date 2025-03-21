package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Administrator;

public interface AdminService {
	
	String deleteDoctorById(UUID doctorId);

	Administrator completeProfile(UUID adminId, Administrator admin);
	
	AddressDTO getAddressByAdminId(UUID patientId);

}
