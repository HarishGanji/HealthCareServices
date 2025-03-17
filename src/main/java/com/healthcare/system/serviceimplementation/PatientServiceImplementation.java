package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.UserRepository;
import com.healthcare.system.security.JWTutil;
import com.healthcare.system.service.PatientService;

@Service
public class PatientServiceImplementation implements PatientService {

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	JWTutil jwtUtil;

	@Override
	public Patient completeProfile(UUID patientId, Patient patient, String token) {
		String userId = jwtUtil.extractUserId(token);
		Patient pat = patientRepo.getPatientById(patientId);
		if (pat.getUser() != null && pat != null && pat.getUser().getUserId().toString().equals(userId)) {
			User user = pat.getUser();
			pat.setPatientName(patient.getPatientName());
			pat.setAge(patient.getAge());
			pat.setGender(patient.getGender());
			pat.setContactNumber(patient.getContactNumber());
			pat.setDateOfBirth(patient.getDateOfBirth());
			pat.setEmail(patient.getEmail());
			pat.setHealthIssue(patient.getHealthIssue());
			user.setEmail(patient.getEmail());
			user.setPhoneNumber(patient.getContactNumber());
			pat.setUser(user);
			return patientRepo.save(pat);
		} else {
			throw new RuntimeException("User NOT Found");
		}

	}
}
