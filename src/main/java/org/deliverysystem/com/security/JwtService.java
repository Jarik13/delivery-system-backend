package org.deliverysystem.com.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private static final String TOKEN_TYPE = "token_type";

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${app.security.jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${app.security.jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    public JwtService() throws Exception {
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
        this.publicKey  = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(TOKEN_TYPE, "access_token");
        return buildToken(userDetails.getUsername(), claims, accessTokenExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of(TOKEN_TYPE, "refresh_token");
        return buildToken(userDetails.getUsername(), claims, refreshTokenExpiration);
    }

    public String refreshAccessToken(String refreshToken) {
        Claims claims = extractClaims(refreshToken);
        if (!"refresh_token".equals(claims.get(TOKEN_TYPE)))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Невалідний тип токену");
        if (isTokenExpired(refreshToken))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token протермінований");

        return buildToken(claims.getSubject(), Map.of(TOKEN_TYPE, "access_token"), accessTokenExpiration);
    }

    public boolean isTokenValid(String token, String expectedEmail) {
        try {
            String email = extractEmail(token);
            return email.equals(expectedEmail) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    private String buildToken(String email, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .subject(email)
                .claims(claims)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expiration)))
                .signWith(privateKey)
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Невалідний JWT токен");
        }
    }
}