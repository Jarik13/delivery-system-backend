package org.deliverysystem.com.export.impl;

import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteSheetItemDto;
import org.deliverysystem.com.export.RouteListExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.List;

@Component
public class CsvRouteListExportStrategy implements RouteListExportStrategy {
    @Override
    public String getFormat() { return "csv"; }

    @Override
    public ResponseEntity<byte[]> export(List<RouteListDto> routeLists) {
        StringWriter sw = new StringWriter();

        sw.write('\uFEFF');
        sw.write("Номер листа;Кур'єр;Статус;Вага (кг);Плановий виїзд;Дата створення\n");

        for (RouteListDto rl : routeLists) {
            sw.write(String.format("%s;%s;%s;%s;%s;%s\n",
                    csv("ML-" + rl.number()),
                    csv(rl.courierFullName()),
                    csv(rl.statusName()),
                    csv(rl.totalWeight()),
                    csv(rl.plannedDepartureTime()),
                    csv(rl.createdAt())
            ));

            if (rl.items() != null && !rl.items().isEmpty()) {
                sw.write(";#;Трек-номер;Отримувач;Адреса;Телефон;Вага;ЦОД (грн);Статус вручення;Час вручення\n");
                int seq = 1;
                for (RouteSheetItemDto s : rl.items()) {
                    sw.write(String.format(";;%s;%s;%s;%s;%s;%s;%s;%s\n",
                            csv(seq++),
                            csv(s.trackingNumber()),
                            csv(s.recipientFullName()),
                            csv(s.deliveryAddress()),
                            csv(s.recipientPhone()),
                            csv(s.weight()),
                            csv(s.codAmount()),
                            csv(s.isDelivered() ? "Доставлено" : "В процесі"),
                            csv(s.deliveredAt())
                    ));
                }
            }
        }

        byte[] bytes = sw.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=route-lists.csv")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(bytes);
    }

    private String csv(Object value) {
        if (value == null) return "";
        String s = value.toString().replace("\"", "\"\"");
        return s.contains(";") || s.contains("\n") ? "\"" + s + "\"" : s;
    }
}