package com.healthcare.system.service;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.models.User;

public interface UserService {
	
	AuthResponse register(RegisterDTO user);

    AuthResponse login(AuthRequest loginRequest);

}
