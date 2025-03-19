package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;

public interface AddressService {
	
	Address addAddress(UUID patientId, Address address);
	
	AddressDTO getAddressByPatientId(UUID patientId);
	
}
