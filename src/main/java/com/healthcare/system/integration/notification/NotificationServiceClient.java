package com.healthcare.system.integration.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.healthcare.system.integration.dto.NotificationRequestDTO;
import com.healthcare.system.integration.dto.NotificationResponseDTO;

/**
 * Feign client for notification-service.
 */
@FeignClient(name = "notification-service", url = "${services.notification.url:http://localhost:9091}")
public interface NotificationServiceClient {

	@PostMapping("/notifications/send")
	NotificationResponseDTO sendNotification(@RequestBody NotificationRequestDTO request);
}
