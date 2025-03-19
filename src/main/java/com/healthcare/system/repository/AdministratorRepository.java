package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {

	
	@Query(value = "SELECT admin_id FROM Administrator WHERE user_id = :userId", nativeQuery = true)
	UUID getAdministratorByUserId(@Param("userId") UUID userId);

	
}
