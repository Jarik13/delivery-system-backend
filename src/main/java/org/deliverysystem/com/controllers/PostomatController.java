package org.deliverysystem.com.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.deliverysystem.com.dtos.PostomatDto;
import org.deliverysystem.com.services.impl.PostomatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/postomats")
@Tag(name = "Postomats", description = "Управління поштоматами")
public class PostomatController extends AbstractBaseController<PostomatDto, Integer> {
    private final PostomatService postomatService;

    public PostomatController(PostomatService service) {
        super(service);
        this.postomatService = service;
    }

    @Operation(summary = "Знайти поштомати у місті")
    @GetMapping("/by-city/{cityId}")
    public ResponseEntity<Page<PostomatDto>> getByCity(@PathVariable Integer cityId, Pageable pageable) {
        return ResponseEntity.ok(postomatService.findAllByCityId(cityId, pageable));
    }
}