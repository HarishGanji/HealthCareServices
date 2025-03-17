package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.models.Doctor;
import com.healthcare.system.repository.AdministratorRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.AdminService;

import jakarta.transaction.Transactional;

@Service
public class AdminServiceImplementation implements AdminService {

	@Autowired
	AdministratorRepository adminRepo;

	@Autowired
	DoctorRepository doctorRepo;

	@Transactional
	@Override
	public String deleteDoctorById(UUID doctorId) {
		Doctor doc = doctorRepo.getDoctorById(doctorId);
		if (doc != null) {
			doctorRepo.delete(doc);
		} else {
			return "Not Deleted";
		}
		return "successfully deleted";
	}

}
