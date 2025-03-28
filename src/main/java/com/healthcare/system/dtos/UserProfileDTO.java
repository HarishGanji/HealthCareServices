package com.healthcare.system.dtos;

import java.util.UUID;

import com.healthcare.system.enums.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO {

	private UUID uuid;
	@Enumerated(EnumType.STRING)
	private Role role;
}
