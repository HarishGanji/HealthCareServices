package com.healthcare.system.integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payload sent to notification-service for delivering user notifications.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
	private String recipientEmail;
	private String subject;
	private String message;
	private String channel;
}
