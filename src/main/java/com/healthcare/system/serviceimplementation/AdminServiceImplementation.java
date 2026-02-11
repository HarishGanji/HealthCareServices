package com.healthcare.system.serviceimplementation;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.exception.ResourceNotFoundException;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Department;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.AddressRepository;
import com.healthcare.system.repository.AdministratorRepository;
import com.healthcare.system.repository.DepartmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.UserRepository;
import com.healthcare.system.service.AdminService;

import jakarta.transaction.Transactional;

/**
 * Admin service implementation for administrative workflows.
 */
@Service
public class AdminServiceImplementation implements AdminService {

	@Autowired
	AdministratorRepository adminRepo;

	@Autowired
	DoctorRepository doctorRepo;

	@Autowired
	AddressRepository addressRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	DepartmentRepository depRepo;

	@Transactional
	@Override
	public String deleteDoctorById(UUID doctorId) {
		Doctor doc = doctorRepo.getDoctorById(doctorId);
		if (doc == null) {
			throw new ResourceNotFoundException("Doctor not found");
		}
		Department depart = depRepo.getDepartmentById(doc.getDepartment().getDepartmentId());
		depart.setHeadDoctor(null);// removing dependency
		User us = userRepo.getUserById(doc.getUser().getUserId());
		if (us == null) {
			throw new ResourceNotFoundException("User not found");
		}
		doctorRepo.delete(doc);
		userRepo.delete(us);
		return "Successfully Deleted";
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
			throw new ResourceNotFoundException("User not found");
		}

	}

	@Override
	public AddressDTO getAddressByAdminId(UUID adminId) {
		return adminRepo.findById(adminId).map(Administrator::getAddress)
				.map(address -> addressRepo.findById(address.getAddressId())
						.map(addr -> new AddressDTO(addr.getAddressId(), addr.getStreet(), addr.getCity(),
								addr.getState(), addr.getZipCode(), addr.getCountry()))
						.orElseThrow(() -> new ResourceNotFoundException("Address not found")))
				.orElseThrow(() -> new ResourceNotFoundException("Admin not found"));
	}

}
