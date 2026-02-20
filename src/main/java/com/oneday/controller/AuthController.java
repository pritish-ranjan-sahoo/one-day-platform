package com.oneday.controller;


import com.oneday.dto.*;
import com.oneday.service.AuthService;
import com.oneday.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserDataDto> signUp(@RequestBody SignupRequestDto signupRequestDto){
        SignupResponseDto response = authService.signUp(signupRequestDto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(response.getUserData(),headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDataDto> logIn(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto response = authService.logIn(loginRequestDto);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(response.getUserData(),headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> getNewAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new SessionAuthenticationException("Refresh token missing. Please login again.");
        }
        RefreshTokenDto token = refreshTokenService.getNewAccessTokenFromRefreshToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>(token.getAccessToken(),headers,HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        authService.logout();

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

        return new ResponseEntity<>("Logged out !!!",headers,HttpStatus.OK);
    }
}
