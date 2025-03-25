package com.healthcare.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healthcare.system.dtos.AuthRequest;
import com.healthcare.system.dtos.AuthResponse;
import com.healthcare.system.dtos.RegisterDTO;
import com.healthcare.system.models.User;
import com.healthcare.system.service.UserService;

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
    public ResponseEntity<String> resetPassword(@RequestParam String email, 
                                                @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successful!");
    }
}

