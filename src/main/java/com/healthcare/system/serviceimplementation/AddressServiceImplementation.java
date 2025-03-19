package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;
import com.healthcare.system.models.Patient;
import com.healthcare.system.repository.AddressRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.service.AddressService;

@Service
public class AddressServiceImplementation implements AddressService {

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	AddressRepository addressRepo;

	@Override
	public Address addAddress(UUID patientId, Address address) {
		Patient pat = patientRepo.getPatientById(patientId);
		if (pat == null) {
			throw new IllegalArgumentException("Patient not found");
		}
		Address add = pat.getAddress();
		if (add == null) {
			Address ad = new Address();
			ad.setCity(address.getCity());
			ad.setCountry(address.getCountry());
			ad.setState(address.getState());
			ad.setStreet(address.getStreet());
			ad.setZipCode(address.getZipCode());
			ad.setPatient(pat);
			pat.setAddress(ad);
			return addressRepo.save(ad);
		} else {
			throw new RuntimeException("patient address already exist");
		}
	}

	@Override
	public AddressDTO getAddressByPatientId(UUID patientId) {
	    return patientRepo.findById(patientId)
	        .map(Patient::getAddress)
	        .map(address -> addressRepo.findById(address.getAddressId())
	            .map(addr -> new AddressDTO(
	                addr.getAddressId(),
	                addr.getStreet(),
	                addr.getCity(),
	                addr.getState(),
	                addr.getZipCode(),
	                addr.getCountry()))
	            .orElseThrow(() -> new IllegalArgumentException("Address not found")))
	        .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
	}




}
