package org.deliverysystem.com.export.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.export.RouteListExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonRouteListExportStrategy implements RouteListExportStrategy {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public String getFormat() { return "json"; }

    @Override
    public ResponseEntity<byte[]> export(List<RouteListDto> routeLists) {
        try {
            byte[] bytes = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsBytes(routeLists);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=route-lists.json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bytes);
        } catch (Exception e) {
            throw new RuntimeException("JSON export failed", e);
        }
    }
}