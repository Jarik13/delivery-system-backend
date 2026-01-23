package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ParcelDto;
import org.deliverysystem.com.services.impl.ParcelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parcels")
@Tag(name = "Parcels", description = "Посилки")
public class ParcelController extends AbstractBaseController<ParcelDto, Integer> {
    public ParcelController(ParcelService service) {
        super(service);
    }
}
