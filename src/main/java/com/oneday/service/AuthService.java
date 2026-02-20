package com.oneday.service;

import com.oneday.dto.LoginRequestDto;
import com.oneday.dto.LoginResponseDto;
import com.oneday.dto.SignupRequestDto;
import com.oneday.dto.SignupResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AuthService {
    public SignupResponseDto signUp(SignupRequestDto newUser);
    public LoginResponseDto logIn(LoginRequestDto user);
    public LoginResponseDto handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId);
}