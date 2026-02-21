package com.oneday.service.impl;

import com.oneday.dto.*;
import com.oneday.entity.AppUser;
import com.oneday.entity.RefreshToken;
import com.oneday.entity.type.AccountStatusType;
import com.oneday.entity.type.OAuthProviderType;
import com.oneday.entity.type.RoleType;
import com.oneday.reposiratory.AppUserRepository;
import com.oneday.reposiratory.RefreshTokenRepository;
import com.oneday.service.AuthService;
import com.oneday.service.RefreshTokenService;
import com.oneday.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
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
                .email(newUser.getEmail().toLowerCase())
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


    @Transactional
    @Override
    public LoginResponseDto handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {

        String userMail = oAuth2User.getAttribute("email");
        String providerId = authUtil.getProviderId(oAuth2User, registrationId);
        OAuthProviderType provider = authUtil.getProviderName(registrationId);

        // checking for same id - simple login if found
        AppUser appUser = appUserRepository.findByProviderNameAndProviderId(provider, providerId).orElse(null);

        if (appUser == null) {
            if (userMail != null && !userMail.isBlank()) {
                // checking for same email id - link account if found
                appUser = appUserRepository.findByEmail(userMail.toLowerCase()).orElse(null);
                if (appUser != null) {
                    appUser.setProviderName(provider);
                    appUser.setProviderId(providerId);
                } else {
                    appUser = AppUser.builder()
                            .providerName(provider)
                            .providerId(providerId)
                            .username(userMail.toLowerCase())
                            .email(userMail.toLowerCase())
                            .roles(Set.of(RoleType.USER))
                            .accountStatus(AccountStatusType.ACTIVATED)
                            .build();

                }
            } else {
                appUser = AppUser.builder()
                        .providerName(provider)
                        .providerId(providerId)
                        .username(UUID.randomUUID().toString())
                        .roles(Set.of(RoleType.USER))
                        .accountStatus(AccountStatusType.ACTIVATED)
                        .build();
            }
            appUserRepository.save(appUser);
        } else {
            if(appUser.getEmail()==null && (userMail != null && !userMail.isBlank())){
                appUser.setEmail(userMail.toLowerCase());
                appUserRepository.save(appUser);
            }
        }

        UserDataDto data = modelMapper.map(appUser, UserDataDto.class);
        data.setRole(authUtil.findRole(appUser.getRoles()));
        RefreshTokenDto newToken = refreshTokenService.generateRefreshToken(appUser.getUsername());
        data.setAccessToken(newToken.getAccessToken());

        return LoginResponseDto
                .builder()
                .refreshToken(newToken.getRefreshToken())
                .userData(data)
                .build();
    }

    @Transactional
    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SessionAuthenticationException("User is not logged in");
        }

        String username = authentication.getName();
        refreshTokenRepository.deleteByUser_Username(username);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
