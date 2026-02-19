package com.oneday.reposiratory;

import com.oneday.entity.AppUser;
import com.oneday.entity.RefreshToken;
import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByUser_Username(String username);
    public Optional<RefreshToken> findByToken(String token);

}