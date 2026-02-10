package com.healthcare.system.service;

/**
 * Service contract for generating and validating OTP codes.
 */
public interface OTPService {

	String generateOTP(String email);

	boolean verifyOTP(String email, String otp);

}
