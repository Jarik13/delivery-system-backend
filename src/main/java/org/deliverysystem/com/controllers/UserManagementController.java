package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "User Management", description = "Управління користувачами (тільки SUPER_ADMIN)")
public class UserManagementController {
    private final UserService userService;

    @Operation(summary = "Отримати всіх користувачів")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Створити користувача",
            description = "Створює в Keycloak і БД, надсилає email для встановлення пароля")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Користувача створено"),
            @ApiResponse(responseCode = "400", description = "Невірні дані"),
            @ApiResponse(responseCode = "409", description = "Email вже існує"),
    })
    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @Operation(summary = "Видалити користувача")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Видалено"),
            @ApiResponse(responseCode = "404", description = "Не знайдено"),
    })
    @DeleteMapping("/{keycloakId}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Keycloak ID") @PathVariable String keycloakId) {
        userService.deleteUser(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Надіслати email повторно")
    @PostMapping("/{keycloakId}/resend-email")
    public ResponseEntity<Map<String, String>> resendEmail(
            @Parameter(description = "Keycloak ID") @PathVariable String keycloakId) {
        userService.resendEmail(keycloakId);
        return ResponseEntity.ok(Map.of("message", "Email надіслано"));
    }

    @Operation(summary = "Змінити роль користувача")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Роль змінено"),
            @ApiResponse(responseCode = "400", description = "Невірна роль"),
    })
    @PatchMapping("/{keycloakId}/role")
    public ResponseEntity<Map<String, String>> updateRole(
            @Parameter(description = "Keycloak ID") @PathVariable String keycloakId,
            @RequestBody Map<String, String> body) {
        userService.updateRole(keycloakId, body.get("role"));
        return ResponseEntity.ok(Map.of("message", "Роль оновлено"));
    }
}