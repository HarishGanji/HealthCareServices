package com.healthcare.system.service;

public interface EmailService {

    void sendEmail(String to, String subject, String text);

}
