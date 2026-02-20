package com.oneday.service;

import com.oneday.dto.RefreshTokenDto;

public interface RefreshTokenService {
    public RefreshTokenDto generateRefreshToken(String username);
    public RefreshTokenDto getNewAccessTokenFromRefreshToken(String refreshToken);
}
