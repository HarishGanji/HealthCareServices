package com.healthcare.system.serviceimplementation.notification;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.healthcare.system.client.NotificationServiceClient;
import com.healthcare.system.dtos.NotificationEventRequest;
import com.healthcare.system.service.notification.NotificationDispatchService;

/**
 * Best-effort notification dispatcher using Feign client.
 */
@Service
public class NotificationDispatchServiceImpl implements NotificationDispatchService {

	private static final Logger log = LoggerFactory.getLogger(NotificationDispatchServiceImpl.class);

	private final NotificationServiceClient notificationServiceClient;

	public NotificationDispatchServiceImpl(NotificationServiceClient notificationServiceClient) {
		this.notificationServiceClient = notificationServiceClient;
	}

	@Override
	public void notifyPrescriptionRequestCreated(UUID patientId, UUID doctorId) {
		send(new NotificationEventRequest(
				"ONLINE_PRESCRIPTION_REQUEST_CREATED",
				patientId,
				doctorId,
				"A patient has requested online prescription follow-up."));
	}

	@Override
	public void notifyPrescriptionIssued(UUID patientId, UUID doctorId) {
		send(new NotificationEventRequest(
				"ONLINE_PRESCRIPTION_ISSUED",
				patientId,
				doctorId,
				"Online prescription has been issued by doctor."));
	}

	private void send(NotificationEventRequest request) {
		try {
			notificationServiceClient.sendNotification(request);
		} catch (Exception ex) {
			log.warn("Notification service unavailable. eventType={}", request.getEventType());
		}
	}
}
