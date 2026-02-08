package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.exception.ResourceNotFoundException;
import com.healthcare.system.models.Address;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.repository.AddressRepository;
import com.healthcare.system.repository.AdministratorRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.service.AddressService;

/**
 * Address service implementation that attaches addresses to patient, doctor, or admin profiles.
 */
@Service
public class AddressServiceImplementation implements AddressService {

	@Autowired
	PatientRepository patientRepo;

	@Autowired
	AddressRepository addressRepo;

	@Autowired
	DoctorRepository doctorRepo;

	@Autowired
	AdministratorRepository adminRepo;

//	@Override
//	public Address addAddress(UUID patientId, Address address) {
//		Patient pat = patientRepo.getPatientById(patientId);
//		if (pat == null) {
//			throw new IllegalArgumentException("Patient not found");
//		}
//		Address add = pat.getAddress();
//		if (add == null) {
//			Address ad = new Address();
//			ad.setCity(address.getCity());
//			ad.setCountry(address.getCountry());
//			ad.setState(address.getState());
//			ad.setStreet(address.getStreet());
//			ad.setZipCode(address.getZipCode());
//			ad.setPatient(pat);
//			pat.setAddress(ad);
//			return addressRepo.save(ad);
//		} else {
//			throw new RuntimeException("patient address already exist");
//		}
//	}

	@Override
	public AddressDTO addAddress(UUID entityId, Address address, String entityType) {
		Object obj = getObjectById(entityId, entityType);
		if(obj == null) {
			throw new ResourceNotFoundException("Entity not found");
		}
		if(obj instanceof Patient) {
			Address ad = new Address();
			ad.setCity(address.getCity());
			ad.setCountry(address.getCountry());
			ad.setState(address.getState());
			ad.setStreet(address.getStreet());
			ad.setZipCode(address.getZipCode());
			ad.setPatient((Patient) obj);
			((Patient) obj).setAddress(ad);
			return convertToAddressDto(addressRepo.save(ad));
		}else if(obj instanceof Administrator) {
			Address ad = new Address();
			ad.setCity(address.getCity());
			ad.setCountry(address.getCountry());
			ad.setState(address.getState());
			ad.setStreet(address.getStreet());
			ad.setZipCode(address.getZipCode());
			ad.setAdministrator((Administrator) obj);
			((Administrator) obj).setAddress(ad);
			return convertToAddressDto(addressRepo.save(ad));
		}else if(obj instanceof Doctor) {
			Address ad = new Address();
			ad.setCity(address.getCity());
			ad.setCountry(address.getCountry());
			ad.setState(address.getState());
			ad.setStreet(address.getStreet());
			ad.setZipCode(address.getZipCode());
			ad.setDoctor((Doctor) obj);
			((Doctor) obj).setAddress(ad);
			return convertToAddressDto(addressRepo.save(ad));
		}
		else {
			throw new ResourceNotFoundException("Entity not found");
		}
	}

	private AddressDTO convertToAddressDto(Address add) {
		AddressDTO address = new AddressDTO();
		address.setAddressId(add.getAddressId());
		address.setCity(add.getCity());
		address.setCountry(add.getCountry());
		address.setState(add.getState());
		address.setStreet(add.getStreet());
		address.setZipCode(add.getZipCode());
		return address;
	}

	private Object getObjectById(UUID entityId, String entityType) {
		String role = entityType.toLowerCase();
		if (role.equals("patient")) {
			return patientRepo.getPatientById(entityId);
		} else if (role.equals("doctor")) {
			return doctorRepo.getDoctorById(entityId);
		} else if (role.equals("admin")) {
			return adminRepo.getAdminById(entityId);
		} else {
			return null;
		}

	}

}
