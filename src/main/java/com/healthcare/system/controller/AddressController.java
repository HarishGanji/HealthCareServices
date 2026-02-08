package com.healthcare.system.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;
import com.healthcare.system.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	AddressService addServ;

	@PostMapping("/address/{entityId}")
	public ResponseEntity<AddressDTO> addAddressToPatient(@PathVariable UUID entityId, @RequestBody Address add,
			@RequestParam String role) {
		return new ResponseEntity<>(addServ.addAddress(entityId, add, role),HttpStatus.OK);
	}


//    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")  // Role-based access

}
