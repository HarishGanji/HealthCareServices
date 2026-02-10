package com.healthcare.system.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Address;

/**
 * Repository for persisting and querying {@link Address} entities.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
	
	@Query("SELECT a FROM Address a WHERE a.addressId =: addressId")
	Address getAddressById(@Param("addressId") UUID addressId);
}
