package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.AddressDto;
import org.deliverysystem.com.services.impl.AddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/addresses")
@Tag(name = "Addresses", description = "Управління адресами клієнтів")
public class AddressController extends AbstractBaseController<AddressDto, Integer> {
    public AddressController(AddressService service) {
        super(service);
    }
}