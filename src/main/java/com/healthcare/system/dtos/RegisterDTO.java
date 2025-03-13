package com.healthcare.system.dtos;


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

	private com.healthcare.system.enums.Role role;

	private AddressDTO address;
}
