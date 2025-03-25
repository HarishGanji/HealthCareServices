package com.healthcare.system.service;

public interface OTPService {

	String generateOTP(String email);

	boolean verifyOTP(String email, String otp);

}
