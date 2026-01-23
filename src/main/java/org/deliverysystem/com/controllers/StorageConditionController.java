package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.StorageConditionDto;
import org.deliverysystem.com.services.impl.StorageConditionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage-conditions")
@Tag(name = "Storage Conditions", description = "Умови зберігання")
public class StorageConditionController extends AbstractBaseController<StorageConditionDto, Integer> {
    public StorageConditionController(StorageConditionService service) {
        super(service);
    }
}