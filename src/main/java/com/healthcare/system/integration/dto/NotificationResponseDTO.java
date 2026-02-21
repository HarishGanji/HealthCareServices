package com.healthcare.system.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response returned by notification-service.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {
	private boolean accepted;
	private String trackingId;
	private String status;
}
