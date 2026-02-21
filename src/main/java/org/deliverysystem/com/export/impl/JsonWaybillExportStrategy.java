package org.deliverysystem.com.export.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.export.WaybillExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonWaybillExportStrategy implements WaybillExportStrategy {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public ResponseEntity<byte[]> export(List<WaybillDto> waybills) {
        try {
            byte[] bytes = objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsBytes(waybills);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=waybills.json")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bytes);
        } catch (Exception e) {
            throw new RuntimeException("JSON export failed", e);
        }
    }

    @Override
    public String getFormat() { return "json"; }
}