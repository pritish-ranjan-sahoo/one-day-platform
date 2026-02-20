package com.oneday.error;

import com.sun.jdi.request.DuplicateRequestException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandler {
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorLog> handleUserNotFound(UsernameNotFoundException ex){
        ErrorLog err = new ErrorLog("User not found with username: "+ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorLog> handleAuthenticationException(AuthenticationException ex) {
        ErrorLog apiError = new ErrorLog("Authentication failed: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorLog> handleBadCredentialsException(BadCredentialsException ex){
        ErrorLog err = new ErrorLog("The given data is invalid: "+ex.getMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorLog> handleIllegalArgumentException(IllegalArgumentException ex){
        ErrorLog err = new ErrorLog("Received arguments are invalid for method: "+ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorLog> handleJwtException(JwtException ex) {
        ErrorLog apiError = new ErrorLog("Invalid JWT token: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorLog> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorLog apiError = new ErrorLog("Access denied: Insufficient permissions", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<ErrorLog> handleDuplicateRequestException(DuplicateRequestException ex){
        ErrorLog err = new ErrorLog("User already exists: "+ex.getMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionAuthenticationException.class)
    public ResponseEntity<ErrorLog> handleSessionAuthenticationException(SessionAuthenticationException ex){
        ErrorLog err = new ErrorLog("Session Expired: "+ex.getMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ErrorLog> handleOAuth2AuthenticationException(OAuth2AuthenticationException ex){
        ErrorLog err = new ErrorLog("Authentication Unsuccessful: "+ex.getMessage(),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorLog> handleGenericException(Exception ex) {
        ErrorLog apiError = new ErrorLog("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
