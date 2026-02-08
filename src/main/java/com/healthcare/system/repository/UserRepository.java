package com.healthcare.system.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.User;

/**
 * Repository for user entities used in authentication and profile lookup.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

	Optional<User> findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.userId =:userId")
	User getUserById(@Param("userId") UUID userId);
}
