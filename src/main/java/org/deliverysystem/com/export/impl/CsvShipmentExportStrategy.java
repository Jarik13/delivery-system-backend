package org.deliverysystem.com.export.impl;

import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.export.ShipmentExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.StringWriter;
import java.util.List;

@Component
public class CsvShipmentExportStrategy implements ShipmentExportStrategy {
    @Override
    public String getFormat() { return "csv"; }

    @Override
    public ResponseEntity<byte[]> export(List<ShipmentDto> shipments) {
        StringWriter sw = new StringWriter();
        sw.write('\uFEFF');
        sw.write("Трек-номер;Відправник;Отримувач;Звідки (місто);Пункт відправлення;" +
                 "Куди (місто);Пункт призначення;Тип доставки;Статус;" +
                 "Вага (кг);Оголошена вартість;Доставка;За вагу;Відстань;" +
                 "Коробка;Спец. пакування;Страховка;Всього;" +
                 "Оплачено;Залишок;Оплата відправником;" +
                 "Накладна;Маршрутний лист;Дата створення;Дата видачі\n");

        for (ShipmentDto s : shipments) {
            sw.write(String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n",
                    csv(s.trackingNumber()),
                    csv(s.senderFullName()),
                    csv(s.recipientFullName()),
                    csv(s.originCityName()),
                    csv(s.originLocationName()),
                    csv(s.destinationCityName()),
                    csv(s.destinationLocationName()),
                    csv(s.shipmentTypeName()),
                    csv(s.shipmentStatusName()),
                    csv(s.actualWeight()),
                    csv(s.declaredValue()),
                    csv(s.deliveryPrice()),
                    csv(s.weightPrice()),
                    csv(s.distancePrice()),
                    csv(s.boxVariantPrice()),
                    csv(s.specialPackagingPrice()),
                    csv(s.insuranceFee()),
                    csv(s.totalPrice()),
                    csv(s.totalPaidAmount()),
                    csv(s.remainingAmount()),
                    csv(s.isSenderPay()),
                    csv(s.waybillNumber()),
                    csv(s.routeListNumber()),
                    csv(s.createdAt()),
                    csv(s.issuedAt())
            ));
        }

        byte[] bytes = sw.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shipments.csv")
                .contentType(MediaType.parseMediaType("text/csv;charset=UTF-8"))
                .body(bytes);
    }

    private String csv(Object value) {
        if (value == null) return "";
        String s = value.toString().replace("\"", "\"\"");
        return s.contains(";") || s.contains("\n") ? "\"" + s + "\"" : s;
    }
}