package org.deliverysystem.com.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.auth.AuthResponse;
import org.deliverysystem.com.dtos.auth.LoginRequest;
import org.deliverysystem.com.dtos.auth.RefreshResponse;
import org.deliverysystem.com.security.CookieUtils;
import org.deliverysystem.com.security.JwtService;
import org.deliverysystem.com.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtService jwtService;
    private final CookieUtils cookieUtils;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Невірний email або пароль");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        String accessToken  = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        cookieUtils.setRefreshTokenCookie(response, refreshToken);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        return new AuthResponse(accessToken, request.email(), role);
    }

    @Override
    public RefreshResponse refresh(HttpServletRequest request) {
        String refreshToken = cookieUtils.getRefreshToken(request);

        if (refreshToken == null)
            throw new IllegalArgumentException("Refresh token відсутній або протермінований, будь ласка увійдіть знову");

        String email = jwtService.extractEmail(refreshToken);
        if (email == null)
            throw new IllegalArgumentException("Невалідний refresh token");

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (!jwtService.isTokenValid(refreshToken, email))
            throw new IllegalArgumentException("Refresh token недійсний, будь ласка увійдіть знову");

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        return new RefreshResponse(newAccessToken);
    }

    @Override
    public void logout(HttpServletResponse response) {
        cookieUtils.clearRefreshTokenCookie(response);
    }
}