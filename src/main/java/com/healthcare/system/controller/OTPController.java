package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.service.OTPService;

/**
 * OTP endpoints for password reset verification.
 */
@RestController
@RequestMapping("/auth")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> sendOTP(@PathVariable String email) {
        return ResponseEntity.ok(otpService.generateOTP(email));
    }

    @PostMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<String> verifyOTP(@PathVariable String email, @PathVariable String otp) {
        if (otpService.verifyOTP(email, otp)) {
            return ResponseEntity.ok("OTP Verified! You can now reset your password.");
        }
        return ResponseEntity.badRequest().body("Invalid or expired OTP!");
    }
}
