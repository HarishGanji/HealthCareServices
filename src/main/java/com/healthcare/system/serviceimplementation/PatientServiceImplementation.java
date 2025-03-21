package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.AddressRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.UserRepository;
import com.healthcare.system.service.PatientService;

@Service
public class PatientServiceImplementation implements PatientService {

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	AddressRepository addressRepo;

	@Override
	public Patient completeProfile(UUID patientId, Patient patient) {
		Patient pat = patientRepo.getPatientById(patientId);
		User user = pat.getUser();
		if (user != null && pat != null) {
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

	@Override
	public AddressDTO getAddressByPatientId(UUID patientId) {
		return patientRepo.findById(patientId).map(Patient::getAddress)
				.map(address -> addressRepo.findById(address.getAddressId())
						.map(addr -> new AddressDTO(addr.getAddressId(), addr.getStreet(), addr.getCity(),
								addr.getState(), addr.getZipCode(), addr.getCountry()))
						.orElseThrow(() -> new IllegalArgumentException("Address not found")))
				.orElseThrow(() -> new IllegalArgumentException("Patient not found"));
	}
	
}
