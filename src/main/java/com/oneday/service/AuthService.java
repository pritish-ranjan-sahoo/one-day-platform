package com.oneday.service;

import com.oneday.dto.LoginRequestDto;
import com.oneday.dto.LoginResponseDto;
import com.oneday.dto.SignupRequestDto;
import com.oneday.dto.SignupResponseDto;

public interface AuthService {
    public SignupResponseDto signUp(SignupRequestDto newUser);
    public LoginResponseDto logIn(LoginRequestDto user);
}
