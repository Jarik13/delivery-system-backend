package org.deliverysystem.com.export.impl;

import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.export.WaybillExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.List;

@Component
public class CsvWaybillExportStrategy implements WaybillExportStrategy {
    @Override
    public ResponseEntity<byte[]> export(List<WaybillDto> waybills) {
        StringWriter sw = new StringWriter();

        sw.write('\uFEFF');
        sw.write("Номер;Загальна вага (кг);Об'єм (м³);Кількість відправлень;Створив;Дата створення\n");

        for (WaybillDto w : waybills) {
            sw.write(String.format("%s;%s;%s;%s;%s;%s\n",
                    csv(String.valueOf(w.number())),
                    csv(w.totalWeight()),
                    csv(w.volume()),
                    csv(w.shipmentsCount()),
                    csv(w.createdByName()),
                    csv(w.createdAt())
            ));

            if (w.shipments() != null && !w.shipments().isEmpty()) {
                sw.write(";#;Трек-номер;Відправник;Отримувач;Звідки;Куди;Вага;Вартість;Статус\n");
                for (WaybillShipmentDto s : w.shipments()) {
                    sw.write(String.format(";;%s;%s;%s;%s;%s;%s;%s;%s\n",
                            csv(s.trackingNumber()),
                            csv(s.senderFullName()),
                            csv(s.recipientFullName()),
                            csv(s.originCityName()),
                            csv(s.destinationCityName()),
                            csv(s.actualWeight()),
                            csv(s.totalPrice()),
                            csv(s.shipmentStatusName())
                    ));
                }
            }
        }

        byte[] bytes = sw.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=waybills.csv")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(bytes);
    }

    @Override
    public String getFormat() { return "csv"; }

    private String csv(Object value) {
        if (value == null) return "";
        String s = value.toString().replace("\"", "\"\"");
        return s.contains(";") || s.contains("\n") ? "\"" + s + "\"" : s;
    }
}