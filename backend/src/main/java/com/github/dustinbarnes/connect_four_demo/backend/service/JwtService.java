package com.github.dustinbarnes.connect_four_demo.backend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.dustinbarnes.connect_four_demo.backend.entity.UserEntity;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration:3600000}") // default 1 hour in ms
    private long jwtExpirationMillis;

    private static final MacAlgorithm JWT_ALG = Jwts.SIG.HS256;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity userEntity) {
        String subject = userEntity.getId().toString();
        Map<String, Object> claims = Map.of(
                "username", userEntity.getUsername(),
                "role", "player");
        
        return Jwts.builder()
            .subject(subject)
            .claims(claims)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
            .signWith(getSigningKey(), JWT_ALG)
            .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
