package com.healthcare.system.dtos;

import com.healthcare.system.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {

	private String username;

	private String email;

	private String password;

	private String phoneNumber;

	private Role role;

//	private AddressDTO address;
}
