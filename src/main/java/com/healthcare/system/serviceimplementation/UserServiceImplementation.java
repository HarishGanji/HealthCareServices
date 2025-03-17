package com.healthcare.system.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.enums.Role;
import com.healthcare.system.models.Address;
import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.AdministratorRepository;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.UserRepository;
import com.healthcare.system.security.JWTutil;
import com.healthcare.system.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserRepository userRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JWTutil jwtUtils;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	DoctorRepository doctorRepo;
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	AdministratorRepository adminRepo;

	@Override
	public AuthResponse login(AuthRequest loginRequest) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

			User user = userRepo.findByEmail(loginRequest.getEmail())
					.orElseThrow(() -> new IllegalStateException("User not found after authentication"));

			String token = jwtUtils.generateToken(user);
			return buildSuccessResponse(user, token, "Login Success", 200);

		} catch (AuthenticationException e) {
			return buildErrorResponse(401, "Invalid credentials");
		} catch (Exception e) {
			return buildErrorResponse(500, "Login error: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public AuthResponse register(RegisterDTO registerDTO) {
		try {
			validateEmailUniqueness(registerDTO.getEmail());
			User user = createUserFromDTO(registerDTO);
			User savedUser = userRepo.save(user);

			createRoleSpecificEntity(savedUser, registerDTO);
			String token = jwtUtils.generateToken(savedUser);

			return buildSuccessResponse(savedUser, token, "User registered successfully!", 200);

		} catch (IllegalArgumentException e) {
			return buildErrorResponse(400, e.getMessage());
		} catch (Exception e) {
			return buildErrorResponse(500, "Registration error: " + e.getMessage());
		}
	}

	private void validateEmailUniqueness(String email) {
		userRepo.findByEmail(email).ifPresent(user -> {
			throw new IllegalArgumentException("User already exists with email: " + email);
		});
	}

	private User createUserFromDTO(RegisterDTO dto) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setPhoneNumber(dto.getPhoneNumber());
		user.setRole(Optional.ofNullable(dto.getRole()).orElse(Role.ROLE_PATIENT));
		return user;
	}

//	private Address createAddressFromDTO(RegisterDTO dto) {
//		if (dto.getAddress() == null)
//			return null;
//		Address address = new Address();
//		address.setStreet(dto.getAddress().getStreet());
//		address.setCity(dto.getAddress().getCity());
//		address.setState(dto.getAddress().getState());
//		address.setZipCode(dto.getAddress().getZipcode());
//		return address;
//	}

	private void createRoleSpecificEntity(User user, RegisterDTO dto) {
		switch (user.getRole()) {
		case ROLE_DOCTOR:
			doctorRepo.save(createDoctor(user, dto));
			break;
		case ROLE_ADMIN:
			adminRepo.save(createAdministrator(user, dto));
			break;
		case ROLE_PATIENT:
			patientRepo.save(createPatient(user, dto));
			break;
		default:
			throw new IllegalArgumentException("Unsupported role: " + user.getRole());
		}
	}

	private Doctor createDoctor(User user, RegisterDTO dto) {
		Doctor doctor = new Doctor();
		populateCommonFields(doctor, user);
//		doctor.setAddress(null);
		return doctor;
	}

	private Administrator createAdministrator(User user, RegisterDTO dto) {
		Administrator admin = new Administrator();
		populateCommonFields(admin, user);
//		admin.setAddress(null);
		return admin;
	}

	private Patient createPatient(User user, RegisterDTO dto) {
//		if (dto.getAddress() == null) {
//			throw new IllegalArgumentException("Address is required for patient registration");
//		}
		Patient patient = new Patient();
		populateCommonFields(patient, user);
//		patient.setAddress(null);
		return patient;
	}

	private void populateCommonFields(Object entity, User user) {
		if (entity instanceof Doctor doctor) {
			doctor.setDoctorName(user.getUsername());
			doctor.setEmail(user.getEmail());
			doctor.setContactNumber(user.getPhoneNumber());
			doctor.setUser(user);
		} else if (entity instanceof Administrator admin) {
			admin.setAdminName(user.getUsername());
			admin.setEmail(user.getEmail());
			admin.setPhoneNumber(user.getPhoneNumber());
			admin.setUser(user);
		} else if (entity instanceof Patient patient) {
			patient.setPatientName(user.getUsername());
			patient.setEmail(user.getEmail());
			patient.setContactNumber(user.getPhoneNumber());
			patient.setUser(user);
		}
	}

	private AuthResponse buildSuccessResponse(User user, String token, String message, int statusCode) {
		AuthResponse response = new AuthResponse();
		response.setStatusCode(statusCode);
		response.setToken(token);
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setMessage(message);
		response.setUUID(user.getRole() + ":" + user.getUserId());
		return response;
	}

	private AuthResponse buildErrorResponse(int statusCode, String message) {
		AuthResponse response = new AuthResponse();
		response.setStatusCode(statusCode);
		response.setMessage(message);
		return response;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
}