package com.healthcare.system.service;


import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.dtos.UserProfileDTO;

/**
 * Service contract for authentication and user profile operations.
 */
public interface UserService {
	
	AuthResponse register(RegisterDTO user);

    AuthResponse login(AuthRequest loginRequest);

	void resetPassword(String email, String newPassword);
	
	UserProfileDTO getUserProfile(String email);
}
