package com.healthcare.system.integration.notification;

import org.springframework.stereotype.Service;

import com.healthcare.system.integration.dto.NotificationRequestDTO;

/**
 * Wrapper around notification-service Feign client for healthcare workflows.
 */
@Service
public class NotificationGateway {

	private final NotificationServiceClient notificationServiceClient;

	public NotificationGateway(NotificationServiceClient notificationServiceClient) {
		this.notificationServiceClient = notificationServiceClient;
	}

	public void sendPrescriptionRequestRaised(String doctorEmail, String patientName) {
		notificationServiceClient.sendNotification(new NotificationRequestDTO(
				doctorEmail,
				"New online prescription request",
				"Patient " + patientName + " has raised an online prescription request.",
				"EMAIL"));
	}

	public void sendPrescriptionReady(String patientEmail) {
		notificationServiceClient.sendNotification(new NotificationRequestDTO(
				patientEmail,
				"Your online prescription is ready",
				"Doctor has responded to your request. You can now download the prescription PDF.",
				"EMAIL"));
	}
}
