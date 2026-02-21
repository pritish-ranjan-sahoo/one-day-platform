package com.oneday.service;


import com.oneday.dto.VerifyUserRequestDto;

public interface UserService {
    public void sendOtp(String email);
    public void verifyUser(VerifyUserRequestDto request);
}
