package com.healthcare.system.service;

import java.util.UUID;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;

/**
 * Service contract for associating addresses with domain profiles.
 */
public interface AddressService {
	
//	Address addAddress(UUID patientId, Address address);
		
	AddressDTO addAddress(UUID entityId, Address address, String entityType);
}
