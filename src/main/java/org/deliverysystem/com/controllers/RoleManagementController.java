package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.enums.Role;
import org.deliverysystem.com.services.impl.KeycloakAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
@Tag(name = "Role Management", description = "Управління правами ролей")
public class RoleManagementController {
    private final KeycloakAdminService keycloakAdminService;

    @GetMapping
    public ResponseEntity<List<String>> getRoles() {
        return ResponseEntity.ok(
                Arrays.stream(Role.values())
                        .filter(r -> r != Role.SUPER_ADMIN)
                        .map(Role::name)
                        .toList()
        );
    }

    @GetMapping("/{roleName}/permissions")
    public ResponseEntity<List<String>> getPermissions(@PathVariable String roleName) {
        Map<String, List<String>> attrs = keycloakAdminService.getRoleAttributes(roleName);
        return ResponseEntity.ok(attrs.getOrDefault("permissions", List.of()));
    }

    @PutMapping("/{roleName}/permissions")
    public ResponseEntity<Void> updatePermissions(
            @PathVariable String roleName,
            @RequestBody List<String> permissions) {
        if (roleName.equals(Role.SUPER_ADMIN.name()))
            throw new IllegalArgumentException("Не можна редагувати права SUPER_ADMIN");
        Map<String, List<String>> attrs = new HashMap<>();
        attrs.put("permissions", permissions);
        keycloakAdminService.updateRoleAttributes(roleName, attrs);
        return ResponseEntity.ok().build();
    }
}