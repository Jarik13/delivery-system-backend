package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.clients.ClientDto;
import org.deliverysystem.com.dtos.clients.CreateClientDto;
import org.deliverysystem.com.services.impl.ClientService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "Управління клієнтами")
public class ClientController {
    private final ClientService clientService;

    @Operation(summary = "Отримати всіх клієнтів (з пагінацією)")
    @GetMapping
    public ResponseEntity<RestPage<ClientDto>> getAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(clientService.findAll(pageable));
    }

    @Operation(summary = "Отримати клієнта за ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @Operation(summary = "Створити клієнта")
    @PostMapping
    public ResponseEntity<ClientDto> create(@Valid @RequestBody CreateClientDto dto) {
        return new ResponseEntity<>(clientService.createClient(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити клієнта")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> update(
            @PathVariable Integer id,
            @Valid @RequestBody CreateClientDto dto
    ) {
        return ResponseEntity.ok(clientService.update(id, dto));
    }

    @Operation(summary = "Видалити клієнта")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}