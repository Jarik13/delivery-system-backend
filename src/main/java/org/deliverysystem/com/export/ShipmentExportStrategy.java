package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ShipmentExportStrategy {
    ResponseEntity<byte[]> export(List<ShipmentDto> shipments);
    String getFormat();
}