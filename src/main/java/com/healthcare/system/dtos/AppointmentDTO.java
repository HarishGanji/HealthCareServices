package com.healthcare.system.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.healthcare.system.enums.Status;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
	
	private UUID appointmentId;
	private UUID patient_Id;
	private UUID doctor_Id;
	
    @Enumerated(EnumType.STRING)  // Ensure this is present
	private Status status;

	private LocalDateTime appointmentDateTime;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
}
