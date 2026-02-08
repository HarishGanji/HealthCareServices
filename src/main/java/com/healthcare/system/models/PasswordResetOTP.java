package com.healthcare.system.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * One-time password entity used for password reset flows.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class PasswordResetOTP {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String email;

    private String otp;

    private LocalDateTime expiryTime;
}
