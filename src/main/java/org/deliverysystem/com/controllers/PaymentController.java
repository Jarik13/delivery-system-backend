package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.payments.PaymentDto;
import org.deliverysystem.com.services.impl.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Платежі")
public class PaymentController extends AbstractBaseController<PaymentDto, Integer> {
    private final PaymentService paymentService;

    public PaymentController(PaymentService service) {
        super(service);
        this.paymentService = service;
    }

    @Operation(summary = "Знайти за номером транзакції")
    @GetMapping("/search")
    public ResponseEntity<PaymentDto> findByTransaction(@RequestParam String transactionNumber) {
        return ResponseEntity.ok(paymentService.findByTransactionNumber(transactionNumber));
    }
}
