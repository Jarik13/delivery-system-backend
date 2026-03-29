package org.deliverysystem.com.export;

import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RouteListExportContext {
    private final Map<String, RouteListExportStrategy> strategies;

    public RouteListExportContext(List<RouteListExportStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(RouteListExportStrategy::getFormat, Function.identity()));
    }

    public ResponseEntity<byte[]> export(String format, List<RouteListDto> routeLists) {
        RouteListExportStrategy strategy = strategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Непідтримуваний формат: " + format + ". Доступні: " + strategies.keySet());
        }
        return strategy.export(routeLists);
    }
}