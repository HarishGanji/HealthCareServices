package com.healthcare.system.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.PasswordResetOTP;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordResetOTP, UUID>{
	
	Optional<PasswordResetOTP> findFirstByEmailOrderByExpiryTimeDesc(String email);// This ensures that only the most recent OTP is fetched.
	
	void deleteByEmail(String email);

//	@Query("SELECT p FROM PasswordResetOTP p WHERE p.email = :email ORDER BY p.expiryTime DESC LIMIT 1")
//	Optional<PasswordResetOTP> findLatestOTPByEmail(@Param("email") String email);

}
