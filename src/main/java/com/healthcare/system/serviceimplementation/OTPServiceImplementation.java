package com.healthcare.system.serviceimplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.healthcare.system.models.PasswordResetOTP;
import com.healthcare.system.repository.PasswordResetRepository;
import com.healthcare.system.service.EmailService;
import com.healthcare.system.service.OTPService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPServiceImplementation implements OTPService {

    @Autowired
    private PasswordResetRepository otpRepo;

    @Autowired
    private EmailService emailService;

    private static final int OTP_EXPIRATION_MINUTES = 5;
    
    
    @Override
    public String generateOTP(String email) {
        // Delete old OTP if exists
        otpRepo.deleteByEmail(email);

        // Generate a new OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        PasswordResetOTP passwordResetOTP = new PasswordResetOTP();
        passwordResetOTP.setEmail(email);
        passwordResetOTP.setOtp(otp);
        passwordResetOTP.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));

        otpRepo.save(passwordResetOTP);

        emailService.sendEmail(email, "Password Reset OTP", "Your OTP is: " + otp);

        return "OTP sent to your email!";
    }

    @Override
    public boolean verifyOTP(String email, String otp) {
        Optional<PasswordResetOTP> storedOtp = otpRepo.findFirstByEmailOrderByExpiryTimeDesc(email);

        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(otp) &&
            storedOtp.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            
            otpRepo.delete(storedOtp.get()); // Remove OTP after successful verification
            return true;
        }
        return false;
    }

//    @Override
//    public String generateOTP(String email) {
//        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
//
//        PasswordResetOTP passwordResetOTP = new PasswordResetOTP();
//        passwordResetOTP.setEmail(email);
//        passwordResetOTP.setOtp(otp);
//        passwordResetOTP.setExpiryTime(LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
//
//        otpRepo.save(passwordResetOTP);
//
//        emailService.sendEmail(email, "Password Reset OTP", "Your OTP is: " + otp);
//
//        return "OTP sent to your email!";
//    }

//    @Override
//    public boolean verifyOTP(String email, String otp) {
//        Optional<PasswordResetOTP> storedOtp = otpRepo.findByEmail(email);
//
//        if (storedOtp.isPresent() && storedOtp.get().getOtp().equals(otp) && 
//            storedOtp.get().getExpiryTime().isAfter(LocalDateTime.now())) {
//            
//            otpRepo.delete(storedOtp.get()); // Remove OTP after successful verification
//            return true;
//        }
//        return false;
//    }
    
 


}
