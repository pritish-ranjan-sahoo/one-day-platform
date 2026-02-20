package com.oneday.util;

import com.oneday.dto.LoginResponseDto;
import com.oneday.dto.UserDataDto;
import com.oneday.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler  implements AuthenticationSuccessHandler {

    private final AuthService authService;
    private final ObjectMapper objectMapper;


    @Override
    public void
    onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String registrationId = token.getAuthorizedClientRegistrationId();

        LoginResponseDto responseData = authService.handleOAuth2LoginRequest(oAuth2User,
                registrationId);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", responseData.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(responseData.getUserData()));
    }
}