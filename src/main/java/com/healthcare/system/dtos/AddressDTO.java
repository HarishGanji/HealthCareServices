package com.healthcare.system.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
	private UUID addressId;

	private String street;

	private String city;

	private String state;

	private String zipCode;

	private String country;
}