package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.BoxTypeDto;
import org.deliverysystem.com.services.impl.BoxTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/box-types")
@Tag(name = "Box Types", description = "Типи коробок")
public class BoxTypeController extends AbstractBaseController<BoxTypeDto, Integer> {
    public BoxTypeController(BoxTypeService service) {
        super(service);
    }
}