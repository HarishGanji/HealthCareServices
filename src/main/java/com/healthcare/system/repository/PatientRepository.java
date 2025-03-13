package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

}
