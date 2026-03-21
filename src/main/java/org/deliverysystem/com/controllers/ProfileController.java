package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.annotations.CurrentUser;
import org.deliverysystem.com.dtos.users.CurrentUserDto;
import org.deliverysystem.com.dtos.users.UpdateProfileDto;
import org.deliverysystem.com.dtos.users.UserDto;
import org.deliverysystem.com.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Управління власним профілем")
public class ProfileController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Отримати свій профіль")
    public ResponseEntity<UserDto> getProfile(@CurrentUser CurrentUserDto user) {
        return ResponseEntity.ok(userService.getProfile(user.keycloakId()));
    }

    @PutMapping
    @Operation(summary = "Оновити свій профіль")
    public ResponseEntity<UserDto> updateProfile(
            @CurrentUser CurrentUserDto user,
            @Valid @RequestBody UpdateProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(user.keycloakId(), user.role(), dto));
    }
}