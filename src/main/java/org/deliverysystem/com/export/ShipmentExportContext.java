package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ShipmentExportContext {
    private final Map<String, ShipmentExportStrategy> strategies;

    public ShipmentExportContext(List<ShipmentExportStrategy> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(ShipmentExportStrategy::getFormat, Function.identity()));
    }

    public ResponseEntity<byte[]> export(String format, List<ShipmentDto> shipments) {
        ShipmentExportStrategy strategy = strategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Непідтримуваний формат експорту: " + format);
        }
        return strategy.export(shipments);
    }
}