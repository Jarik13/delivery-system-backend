package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.auth.AuthResponse;
import org.deliverysystem.com.dtos.auth.LoginRequest;
import org.deliverysystem.com.dtos.auth.RefreshResponse;
import org.deliverysystem.com.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Авторизація", description = "Вхід, оновлення токену, вихід")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Вхід в систему")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успішний вхід"),
            @ApiResponse(responseCode = "401", description = "Невірний email або пароль"),
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(request, response));
    }

    @Operation(summary = "Оновлення access token через refresh token з куки")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Новий access token"),
            @ApiResponse(responseCode = "401", description = "Refresh token відсутній або недійсний"),
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(HttpServletRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @Operation(summary = "Вихід з системи — очищає refresh token з куки")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.noContent().build();
    }
}