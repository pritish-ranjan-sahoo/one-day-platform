package com.oneday.service.impl;

import com.oneday.dto.*;
import com.oneday.entity.AppUser;
import com.oneday.entity.type.AccountStatusType;
import com.oneday.entity.type.RoleType;
import com.oneday.reposiratory.AppUserRepository;
import com.oneday.security.JwtUtil;
import com.oneday.service.AuthService;
import com.oneday.service.RefreshTokenService;
import com.oneday.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepository;
    private final AuthUtil authUtil;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    @Override
    public SignupResponseDto signUp(SignupRequestDto newUser) {
        AppUser appUser = AppUser
                .builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .password(passwordEncoder.encode(newUser.getPassword()))
                .roles(Set.of(RoleType.USER))
                .accountStatus(AccountStatusType.ACTIVATED)
                .build();

        appUserRepository.save(appUser);
        UserDataDto data = modelMapper.map(appUser, UserDataDto.class);
        data.setRole(authUtil.findRole(appUser.getRoles()));

        RefreshTokenDto token = refreshTokenService.generateRefreshToken(appUser.getUsername());
        data.setAccessToken(token.getAccessToken());

        SignupResponseDto response = SignupResponseDto
                .builder()
                .refreshToken(token.getRefreshToken())
                .userData(data)
                .build();
        return response;
    }

    @Override
    public LoginResponseDto logIn(LoginRequestDto user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getCredential(), user.getPassword())
        );
        log.info("Authenticated");
        AppUser appUser = appUserRepository
                .findByEmailOrUsername(user.getCredential(), user.getCredential())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User not Found !!")
                );
        log.info("found user!!");

        UserDataDto data = modelMapper.map(appUser, UserDataDto.class);
        data.setRole(authUtil.findRole(appUser.getRoles()));

        RefreshTokenDto token = refreshTokenService.generateRefreshToken(appUser.getUsername());
        data.setAccessToken(token.getAccessToken());

        LoginResponseDto response = LoginResponseDto
                .builder()
                .refreshToken(token.getRefreshToken())
                .userData(data)
                .build();
        return response;
    }
}
