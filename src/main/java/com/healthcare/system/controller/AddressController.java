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
import org.springframework.web.bind.annotation.RestController;

import com.healthcare.system.dtos.AddressDTO;
import com.healthcare.system.models.Address;
import com.healthcare.system.service.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

	@Autowired
	AddressService addServ;

	@PostMapping("/address/{patientId}")
	public ResponseEntity<Address> addAddressToPatient(@PathVariable UUID patientId, @RequestBody Address add) {
		return new ResponseEntity<>(addServ.addAddress(patientId, add), HttpStatus.OK);
	}

	@GetMapping("/address/{patientId}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable UUID patientId) {
		return new ResponseEntity<>(addServ.getAddressByPatientId(patientId), HttpStatus.OK);
	}
//    @PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN')")  // Role-based access

}
