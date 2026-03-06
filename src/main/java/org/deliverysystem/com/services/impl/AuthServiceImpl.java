package org.deliverysystem.com.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.auth.AuthResponse;
import org.deliverysystem.com.dtos.auth.LoginRequest;
import org.deliverysystem.com.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", request.email());
        body.add("password", request.password());

        try {
            ResponseEntity<Map> keycloakResponse = restTemplate.postForEntity(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            Map<String, Object> tokens = keycloakResponse.getBody();
            String accessToken  = (String) tokens.get("access_token");
            String refreshToken = (String) tokens.get("refresh_token");

            ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/api/v1/auth")
                    .maxAge(604800)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            String role = extractRoleFromToken(accessToken);

            return new AuthResponse(accessToken, request.email(), role);
        } catch (HttpClientErrorException e) {
            throw new BadCredentialsException("Невірний email або пароль");
        }
    }

    @Override
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;

        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }

        if (refreshToken == null)
            throw new BadCredentialsException("Refresh token відсутній");

        String tokenUrl = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("refresh_token", refreshToken);

        try {
            ResponseEntity<Map> keycloakResponse = restTemplate.postForEntity(
                    tokenUrl,
                    new HttpEntity<>(body, headers),
                    Map.class
            );

            Map<String, Object> tokens = keycloakResponse.getBody();
            String newAccessToken  = (String) tokens.get("access_token");
            String newRefreshToken = (String) tokens.get("refresh_token");

            ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/api/v1/auth")
                    .maxAge(604800)
                    .sameSite("Strict")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            String role = extractRoleFromToken(newAccessToken);
            String email = extractEmailFromToken(newAccessToken);

            return new AuthResponse(newAccessToken, email, role);
        } catch (HttpClientErrorException e) {
            throw new BadCredentialsException("Refresh token недійсний");
        }
    }

    @Override
    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractRoleFromToken(String accessToken) {
        try {
            String payload = accessToken.split("\\.")[1];
            String decoded = new String(Base64.getUrlDecoder().decode(payload));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.readValue(decoded, Map.class);
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles");
            return roles.stream()
                    .filter(r -> List.of("EMPLOYEE", "COURIER", "DRIVER", "ADMIN", "SUPER_ADMIN").contains(r))
                    .findFirst()
                    .map(r -> "ROLE_" + r)
                    .orElse("ROLE_EMPLOYEE");
        } catch (Exception e) {
            return "ROLE_EMPLOYEE";
        }
    }

    private String extractEmailFromToken(String accessToken) {
        try {
            String payload = accessToken.split("\\.")[1];
            String decoded = new String(Base64.getUrlDecoder().decode(payload));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> claims = mapper.readValue(decoded, Map.class);
            return (String) claims.getOrDefault("email", "");
        } catch (Exception e) {
            return "";
        }
    }
}