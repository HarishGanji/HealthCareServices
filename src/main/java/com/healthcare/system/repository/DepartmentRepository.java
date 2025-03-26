package com.healthcare.system.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthcare.system.models.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

	List<Department> findAll();
	
	
	@Query("SELECT d FROM Department d WHERE d.departmentId = :departmentId")
    Department getDepartmentById(@Param("departmentId") UUID departmentId);

}
