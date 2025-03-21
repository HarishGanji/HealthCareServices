package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Doctor;

public interface DoctorService {

	Doctor addOrEditSpecialization(UUID doctorId, Doctor doctor);

	AddressDTO getAddressByDoctorId(UUID doctorId);

	Doctor completeProfile(UUID doctorId, Doctor doctor);

}
