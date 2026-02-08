package com.healthcare.system.serviceimplementation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Appointment;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.AddressRepository;
import com.healthcare.system.repository.AppointmentRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.service.DoctorService;

@Service
public class DoctorServiceImplementation implements DoctorService {

	@Autowired
	DoctorRepository doctorRepo;

	@Autowired
	AddressRepository addressRepo;

	@Autowired
	AppointmentRepository appointmentRepo;

	@Override
	public Doctor addOrEditSpecialization(UUID doctorId, Doctor doctor) {
		Doctor doc = doctorRepo.getDoctorById(doctorId);
		if (doc != null) {
			doc.setSpecialization(doctor.getSpecialization().toLowerCase());
		}
		return doctorRepo.save(doc);
	}

	@Override
	public AddressDTO getAddressByDoctorId(UUID doctorId) {
		return doctorRepo.findById(doctorId).map(Doctor::getAddress)
				.map(address -> addressRepo.findById(address.getAddressId())
						.map(addr -> new AddressDTO(addr.getAddressId(), addr.getStreet(), addr.getCity(),
								addr.getState(), addr.getZipCode(), addr.getCountry()))
						.orElseThrow(() -> new IllegalArgumentException("Address not found")))
				.orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
	}

	@Override
	public Doctor completeProfile(UUID doctorId, Doctor doctor) {
		Doctor doc = doctorRepo.getDoctorById(doctorId);
		User user = doc.getUser();
		if (user != null && doc != null) {
			doc.setDoctorName(doctor.getDoctorName());
			doc.setEmail(doctor.getEmail());
			doc.setSpecialization(doctor.getSpecialization());
			doc.setContactNumber(doctor.getContactNumber());
			user.setEmail(doctor.getEmail());
			user.setPhoneNumber(doctor.getContactNumber());
			doc.setUser(user);
			return doctorRepo.save(doc);
		} else {
			throw new RuntimeException("User NOT Found");
		}

	}

	@Override
	public List<Doctor> getDoctorsBySpecialization(String specialization) {
		return doctorRepo.doctorsBySpecialization(specialization);
	}

	@Override
	public List<Appointment> viewAllAppointmentsByDoctorId(UUID doctorId) {
		return appointmentRepo.findByDoctorDoctorId(doctorId);
	}

}
