package com.healthcare.system.dtos;


import com.healthcare.system.enums.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String token;
	private String email;
	@Enumerated(EnumType.STRING)
	private Role role;
	private int statusCode;
	private String message;
}
