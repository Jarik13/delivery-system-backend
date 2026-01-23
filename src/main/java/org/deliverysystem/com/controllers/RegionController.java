package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.RegionDto;
import org.deliverysystem.com.services.BaseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "Regions", description = "Управління адміністративними областями")
public class RegionController extends AbstractBaseController<RegionDto, Integer> {
    public RegionController(BaseService<RegionDto, Integer> regionService) {
        super(regionService);
    }
}