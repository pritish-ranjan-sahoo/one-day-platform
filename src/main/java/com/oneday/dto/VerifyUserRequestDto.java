package com.oneday.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyUserRequestDto {
    String otp;
    String username;
    String email;
    String verifyAs;
}
