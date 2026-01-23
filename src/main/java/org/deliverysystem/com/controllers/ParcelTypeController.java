package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ParcelTypeDto;
import org.deliverysystem.com.services.impl.ParcelTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parcel-types")
@Tag(name = "Parcel Types", description = "Типи посилок")
public class ParcelTypeController extends AbstractBaseController<ParcelTypeDto, Integer> {
    public ParcelTypeController(ParcelTypeService service) {
        super(service);
    }
}