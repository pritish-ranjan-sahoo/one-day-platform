package com.oneday.service.impl;

import com.oneday.dto.RefreshTokenDto;
import com.oneday.entity.AppUser;
import com.oneday.entity.RefreshToken;
import com.oneday.reposiratory.AppUserRepository;
import com.oneday.reposiratory.RefreshTokenRepository;
import com.oneday.security.JwtUtil;
import com.oneday.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AppUserRepository appUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    public RefreshTokenDto generateRefreshToken(String username){
        RefreshToken refreshToken = refreshTokenRepository.findByUser_Username(username).orElse(null);
        AppUser appUser = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found while generating refresh token")
        );
        if(refreshToken==null){
            refreshToken = RefreshToken.builder().user(appUser).build();
        }

        refreshToken.setCreationDate(LocalDateTime.now());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));

        String newRefreshToken = UUID.randomUUID().toString();
        refreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtUtil.generateJwtToken(appUser);

        return RefreshTokenDto
                .builder()
                .refreshToken(refreshToken.getToken())
                .accessToken(accessToken)
                .build();
    }

    @Override
    public RefreshTokenDto getNewAccessTokenFromRefreshToken(String refreshToken){
        RefreshToken tokenFoundInDb = refreshTokenRepository.findByToken(refreshToken).orElseThrow(
                () -> new SessionAuthenticationException("Refresh Token found invalid")
        );
        if (tokenFoundInDb.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.deleteById(tokenFoundInDb.getTokenId());
            throw new SessionAuthenticationException("Refresh Token found expired");
        }
        String newAccessToken = jwtUtil.generateJwtToken(tokenFoundInDb.getUser());
        String newRefreshToken = UUID.randomUUID().toString();
        tokenFoundInDb.setToken(newRefreshToken);
        tokenFoundInDb.setCreationDate(LocalDateTime.now());
        tokenFoundInDb.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(tokenFoundInDb);

        return RefreshTokenDto
                .builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
