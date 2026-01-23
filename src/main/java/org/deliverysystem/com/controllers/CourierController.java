package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.CourierDto;
import org.deliverysystem.com.services.impl.CourierService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/couriers")
@Tag(name = "Couriers", description = "Кур'єри адресної доставки")
public class CourierController extends AbstractBaseController<CourierDto, Integer> {
    public CourierController(CourierService service) {
        super(service);
    }
}