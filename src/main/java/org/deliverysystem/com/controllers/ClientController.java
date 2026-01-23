package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ClientDto;
import org.deliverysystem.com.services.impl.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@Tag(name = "Clients", description = "Управління клієнтами")
public class ClientController extends AbstractBaseController<ClientDto, Integer> {
    private final ClientService clientService;

    public ClientController(ClientService service) {
        super(service);
        this.clientService = service;
    }

    @Operation(summary = "Знайти клієнта за номером телефону")
    @GetMapping("/search")
    public ResponseEntity<ClientDto> findByPhone(@RequestParam String phone) {
        return ResponseEntity.ok(clientService.findByPhoneNumber(phone));
    }
}