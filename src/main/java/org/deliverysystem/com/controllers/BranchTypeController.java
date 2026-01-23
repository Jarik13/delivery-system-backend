package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.BranchTypeDto;
import org.deliverysystem.com.services.impl.BranchTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/branch-types")
@Tag(name = "Branch Types", description = "Типи відділень (вантажне/поштове)")
public class BranchTypeController extends AbstractBaseController<BranchTypeDto, Integer> {
    public BranchTypeController(BranchTypeService service) {
        super(service);
    }
}