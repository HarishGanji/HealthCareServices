package com.healthcare.system.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrator {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID adminId;

	private String adminName;

	private String phoneNumber;

	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
	@JsonIgnore
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
	@JsonIgnore
    private Doctor doctor;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
 	@JoinColumn(name = "user_id", nullable = false)
	@JsonIgnore
	private User user;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "address_id")
	@JsonIgnore
	private Address address;
}
