package com.oneday.security;

import com.oneday.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    private SecretKey createSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateJwtToken(AppUser user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("status", user.getAccountStatus())
                .signWith(createSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))   // expire after 15mins
                .compact();
    }

    public String verifyToken(String token) {
        Claims claims = Jwts
                .parser()
                .verifyWith(createSecretKey())
                .build().parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }



}
