package com.healthcare.system.service;

/**
 * Service contract for sending outbound emails.
 */
public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
