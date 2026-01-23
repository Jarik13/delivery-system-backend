package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.PaymentTypeDto;
import org.deliverysystem.com.services.impl.PaymentTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payment-types")
@Tag(name = "Payment Types", description = "Способи оплати")
public class PaymentTypeController extends AbstractBaseController<PaymentTypeDto, Integer> {
    public PaymentTypeController(PaymentTypeService service) {
        super(service);
    }
}