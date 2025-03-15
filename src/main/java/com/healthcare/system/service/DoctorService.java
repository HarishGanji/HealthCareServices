package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.models.Doctor;

public interface DoctorService {

	Doctor addOrEditSpecialization(UUID doctorId, Doctor doctor);
}
