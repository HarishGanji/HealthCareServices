package com.healthcare.system.service;

import java.util.List;
import java.util.UUID;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.models.Patient;
import com.healthcare.system.models.User;

public interface UserService {
	
	AuthResponse register(RegisterDTO user);

    AuthResponse login(AuthRequest loginRequest);

}
