package com.healthcare.system.service.notification;

import java.util.UUID;

public interface NotificationDispatchService {
	void notifyPrescriptionRequestCreated(UUID patientId, UUID doctorId);
	void notifyPrescriptionIssued(UUID patientId, UUID doctorId);
}
