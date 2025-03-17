package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Administrator;
import com.healthcare.system.models.Doctor;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {

}
