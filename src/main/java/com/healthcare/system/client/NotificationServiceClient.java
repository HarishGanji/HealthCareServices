package com.healthcare.system.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.healthcare.system.dtos.NotificationEventRequest;

/**
 * Feign client bridge to notification-service.
 */
@FeignClient(name = "notification-service", url = "${notification.service.url}")
public interface NotificationServiceClient {

	@PostMapping("/notifications/send")
	void sendNotification(@RequestBody NotificationEventRequest request);
}
