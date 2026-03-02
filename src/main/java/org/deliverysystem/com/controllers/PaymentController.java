package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.dtos.payments.PaymentStatisticDto;
import org.deliverysystem.com.dtos.search.PaymentSearchCriteria;
import org.deliverysystem.com.dtos.shipments.ShipmentStatisticsDto;
import org.deliverysystem.com.services.impl.PaymentService;
import org.deliverysystem.com.utils.RestPage;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Платежі")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Отримати всі платежі з фільтрацією")
    @GetMapping
    public ResponseEntity<RestPage<PaymentDto>> getAll(
            @ParameterObject PaymentSearchCriteria criteria,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(paymentService.findAll(criteria, pageable));
    }

    @Operation(summary = "Отримати статистику по відправленням (мін/макс значення)")
    @GetMapping("/statistics")
    public ResponseEntity<PaymentStatisticDto> getStatistics() {
        return ResponseEntity.ok(paymentService.getStatistics());
    }

    @Operation(summary = "Отримати платіж за ID")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @Operation(summary = "Створити платіж")
    @PostMapping
    public ResponseEntity<PaymentDto> create(@Valid @RequestBody PaymentDto dto) {
        return new ResponseEntity<>(paymentService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Оновити платіж")
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@PathVariable Integer id,
                                             @Valid @RequestBody PaymentDto dto) {
        return ResponseEntity.ok(paymentService.update(id, dto));
    }

    @Operation(summary = "Видалити платіж")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}