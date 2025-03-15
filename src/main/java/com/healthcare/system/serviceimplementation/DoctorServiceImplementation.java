package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.models.Doctor;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.DoctorService;

@Service
public class DoctorServiceImplementation implements DoctorService {

	@Autowired
	DoctorRepository doctorRepo;

	@Override
	public Doctor addOrEditSpecialization(UUID doctorId, Doctor doctor) {
		Doctor doc = doctorRepo.getDoctorById(doctorId);
		if (doc != null) {
			doc.setSpecialization(doctor.getSpecialization());
		}
		return doctorRepo.save(doc);
	}

}
