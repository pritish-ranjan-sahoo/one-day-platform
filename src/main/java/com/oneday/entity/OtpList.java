package com.oneday.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtpList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long verificationId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String otp;

    @Column(updatable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

}
