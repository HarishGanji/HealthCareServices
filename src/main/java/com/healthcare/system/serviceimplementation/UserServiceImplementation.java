package com.healthcare.system.serviceimplementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.models.Address;
import com.healthcare.system.models.Doctor;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;
import com.healthcare.system.repository.DoctorRepository;
import com.healthcare.system.repository.PatientRepository;
import com.healthcare.system.repository.UserRepository;
import com.healthcare.system.security.JWTutil;
import com.healthcare.system.service.UserService;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTutil jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager; 

	@Autowired
	private DoctorRepository doctorRepo;

	@Autowired
	private PatientRepository patientRepo;

	@Override 
	public AuthResponse login(AuthRequest loginRequest) {
		AuthResponse response = new AuthResponse();
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new NotFoundException());

			String token = jwtUtils.generateToken(user);
			response.setStatusCode(200);
			response.setToken(token);
			response.setEmail(user.getEmail());
			response.setRole(user.getRole());
			response.setMessage("Login Success");
		} catch (NotFoundException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("Error occured while login the user" + e.getMessage());
		}
		return response;
	}

	@Override
	public AuthResponse register(RegisterDTO registerDTO) {
		AuthResponse response = new AuthResponse();

		try {
			// Check if user already exists
			Optional<User> existingUser = userRepo.findByEmail(registerDTO.getEmail());
			if (existingUser.isPresent()) {
				throw new IllegalArgumentException("User already exists with email: " + registerDTO.getEmail());
			}

			// Create and save User entity
			User user = new User();
			user.setUsername(registerDTO.getUsername());
			user.setEmail(registerDTO.getEmail());
			user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
			user.setPhoneNumber(registerDTO.getPhoneNumber());
			user.setRole(registerDTO.getRole() != null ? registerDTO.getRole()
					: com.healthcare.system.enums.Role.ROLE_PATIENT);

			User savedUser = userRepo.save(user);

			// Map User to Doctor or Patient entity based on Role
			if (user.getRole() == com.healthcare.system.enums.Role.ROLE_DOCTOR) {
				Doctor doctor = new Doctor();
				doctor.setDoctorName(user.getUsername());
				doctor.setEmail(user.getEmail());
				doctor.setContactNumber(user.getPhoneNumber());
				doctor.setUser(savedUser);
				doctorRepo.save(doctor);
			} else if (user.getRole() == com.healthcare.system.enums.Role.ROLE_PATIENT) {
				Patient patient = new Patient();
				patient.setPatientName(user.getUsername());
				patient.setEmail(user.getEmail());
				patient.setContactNumber(user.getPhoneNumber());
				patient.setUser(savedUser);

				// Handle Address
				if (registerDTO.getAddress() != null) {
					Address address = new Address();
					address.setStreet(registerDTO.getAddress().getStreet());
					address.setCity(registerDTO.getAddress().getCity());
					address.setState(registerDTO.getAddress().getState());
					address.setZipCode(registerDTO.getAddress().getZipcode());

					// Save the address and assign to patient
					patient.setAddress(address);
				} else {
					throw new IllegalArgumentException("Address is required for patient registration.");
				}

				patientRepo.save(patient);
			}

			// Generate Token after registration
			String token = jwtUtils.generateToken(savedUser);

			response.setStatusCode(200);
			response.setMessage("User registered successfully!");
			response.setEmail(savedUser.getEmail());
			response.setRole(savedUser.getRole());
			response.setToken(token);

		} catch (IllegalArgumentException e) {
			response.setStatusCode(400);
			response.setMessage(e.getMessage());
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("Internal Server Error while registering the user: " + e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

}
