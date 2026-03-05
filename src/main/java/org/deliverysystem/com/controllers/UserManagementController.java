package org.deliverysystem.com.controllers;

import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.users.CreateUserDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.services.impl.KeycloakAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserManagementController {
    private final KeycloakAdminService keycloakAdminService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(keycloakAdminService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@RequestBody CreateUserDto dto) {
        String keycloakId = keycloakAdminService.createUser(dto);
        return ResponseEntity.ok(Map.of(
                "keycloakId", keycloakId,
                "message", "Користувача створено. Email надіслано на " + dto.email()
        ));
    }

    @DeleteMapping("/{keycloakId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String keycloakId) {
        keycloakAdminService.deleteUser(keycloakId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{keycloakId}/resend-email")
    public ResponseEntity<Void> resendEmail(@PathVariable String keycloakId) {
        keycloakAdminService.resendVerificationEmail(keycloakId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{keycloakId}/role")
    public ResponseEntity<Void> updateRole(
            @PathVariable String keycloakId,
            @RequestBody Map<String, String> body) {
        keycloakAdminService.updateUserRole(keycloakId, body.get("role"));
        return ResponseEntity.ok().build();
    }
}