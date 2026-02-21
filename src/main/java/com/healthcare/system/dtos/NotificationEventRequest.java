package com.healthcare.system.dtos;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload sent to notification microservice.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEventRequest {
	private String eventType;
	private UUID patientId;
	private UUID doctorId;
	private String message;
}
