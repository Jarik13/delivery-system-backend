package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.ReturnReasonDto;
import org.deliverysystem.com.services.impl.ReturnReasonService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/return-reasons")
@Tag(name = "Return Reasons", description = "Причини повернення")
public class ReturnReasonController extends AbstractBaseController<ReturnReasonDto, Integer> {
    public ReturnReasonController(ReturnReasonService service) {
        super(service);
    }
}