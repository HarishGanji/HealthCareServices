package com.healthcare.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Enables Feign clients for inter-service communication.
 */
@Configuration
@EnableFeignClients(basePackages = "com.healthcare.system.integration")
public class FeignConfig {
}
