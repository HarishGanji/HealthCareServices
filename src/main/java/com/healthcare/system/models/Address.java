package com.healthcare.system.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID addressId;

	private String street;

	private String city;
	
	private String state;

	private String zipCode;

	private String country;

	@OneToOne(mappedBy = "address")
	@JsonIgnore
	private Doctor doctor;

	@OneToOne(mappedBy = "address")
	@JsonIgnore
	private Patient patient;
}
