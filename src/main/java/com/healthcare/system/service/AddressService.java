package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.models.Address;

public interface AddressService {
	
	Address addAddress(UUID patientId, Address Address);
	
	Address getAddressByPatientId(UUID patientId);
	
}
