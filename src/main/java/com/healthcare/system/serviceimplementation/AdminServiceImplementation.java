package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.AddressRepository;
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

	@Autowired
	AddressRepository addressRepo;
	
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

	@Override
	public Administrator completeProfile(UUID adminId, Administrator admin) {
		Administrator ad = adminRepo.getAdminById(adminId);
		User user = ad.getUser();
		if (user != null && ad != null) {
			ad.setAdminName(admin.getAdminName());
			ad.setEmail(admin.getEmail());
			ad.setPhoneNumber(admin.getPhoneNumber());
			user.setEmail(admin.getEmail());
			user.setPhoneNumber(admin.getPhoneNumber());
			ad.setUser(user);
			return adminRepo.save(ad);
		} else {
			throw new RuntimeException("User NOT Found");
		}

	}

	@Override
	public AddressDTO getAddressByAdminId(UUID adminId) {
		return adminRepo.findById(adminId).map(Administrator::getAddress)
				.map(address -> addressRepo.findById(address.getAddressId())
						.map(addr -> new AddressDTO(addr.getAddressId(), addr.getStreet(), addr.getCity(),
								addr.getState(), addr.getZipCode(), addr.getCountry()))
						.orElseThrow(() -> new IllegalArgumentException("Address not found")))
				.orElseThrow(() -> new IllegalArgumentException("Admin not found"));
	}
}
