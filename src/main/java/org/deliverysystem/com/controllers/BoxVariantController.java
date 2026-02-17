package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.BoxVariantDto;
import org.deliverysystem.com.services.impl.BoxVariantService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/box-variants")
@Tag(name = "Box Variants", description = "Управління варіантами розмірів коробок")
public class BoxVariantController extends AbstractBaseController<BoxVariantDto, Integer> {
    public BoxVariantController(BoxVariantService service) {
        super(service);
    }
}