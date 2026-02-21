package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WaybillExportContext {
    private final Map<String, WaybillExportStrategy> strategies;

    public WaybillExportContext(List<WaybillExportStrategy> strategyList) {
        this.strategies = strategyList.stream().collect(Collectors.toMap(WaybillExportStrategy::getFormat, Function.identity()));
    }

    public ResponseEntity<byte[]> export(String format, List<WaybillDto> waybills) {
        WaybillExportStrategy strategy = strategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Непідтримуваний формат: " + format + ". Доступні: " + strategies.keySet());
        }
        return strategy.export(waybills);
    }

    public List<String> getSupportedFormats() {
        return List.copyOf(strategies.keySet());
    }
}