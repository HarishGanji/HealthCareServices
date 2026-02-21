package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.dtos.UserProfileDTO;
import com.healthcare.system.models.User;
import com.healthcare.system.service.UserService;

/**
 * Authentication and user profile endpoints for registration, login, and profile lookup.
 */
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * User Registration Endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterDTO user) {
        AuthResponse response = userService.register(user);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * User Login Endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest loginRequest) {
        AuthResponse response = userService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    
    
    @PostMapping("/reset-password")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<String> resetPassword(@RequestParam String email, 
                                                @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successful!");
    }
    
    @GetMapping("/role-based-user")
    @org.springframework.security.access.prepost.PreAuthorize("hasAnyAuthority('ADMIN', 'DOCTOR', 'PATIENT')")
    public ResponseEntity<UserProfileDTO> getUserProfile(@RequestParam String email) {
        UserProfileDTO profileDTO = userService.getUserProfile(email);
        return ResponseEntity.ok(profileDTO);
    }
}
