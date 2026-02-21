package org.deliverysystem.com.export.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.export.WaybillExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelWaybillExportStrategy implements WaybillExportStrategy {
    @Override
    public ResponseEntity<byte[]> export(List<WaybillDto> waybills) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Накладні");
            sheet.setDefaultColumnWidth(20);

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle waybillStyle = wb.createCellStyle();
            Font waybillFont = wb.createFont();
            waybillFont.setBold(true);
            waybillStyle.setFont(waybillFont);
            waybillStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            waybillStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle shipmentStyle = wb.createCellStyle();
            shipmentStyle.setIndention((short) 2);

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            String[] cols = {"Номер", "Вага (кг)", "Об'єм (м³)", "Відправлень", "Створив", "Дата створення"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            for (WaybillDto w : waybills) {
                Row wRow = sheet.createRow(rowIdx++);
                wRow.createCell(0).setCellValue(w.number() != null ? w.number() : 0);
                wRow.createCell(1).setCellValue(w.totalWeight() != null ? w.totalWeight().doubleValue() : 0);
                wRow.createCell(2).setCellValue(w.volume() != null ? w.volume().doubleValue() : 0);
                wRow.createCell(3).setCellValue(w.shipmentsCount() != null ? w.shipmentsCount() : 0);
                wRow.createCell(4).setCellValue(w.createdByName() != null ? w.createdByName() : "");
                wRow.createCell(5).setCellValue(w.createdAt() != null ? w.createdAt().toString() : "");
                for (int i = 0; i < 6; i++) wRow.getCell(i).setCellStyle(waybillStyle);

                if (w.shipments() != null && !w.shipments().isEmpty()) {
                    Row shipHeader = sheet.createRow(rowIdx++);
                    String[] sCols = {"#", "Трек-номер", "Відправник", "Отримувач", "Звідки", "Куди", "Вага", "Вартість", "Статус"};
                    for (int i = 0; i < sCols.length; i++) {
                        Cell c = shipHeader.createCell(i);
                        c.setCellValue(sCols[i]);
                        c.setCellStyle(headerStyle);
                    }

                    for (WaybillShipmentDto s : w.shipments()) {
                        Row sRow = sheet.createRow(rowIdx++);
                        sRow.createCell(0).setCellValue(s.sequenceNumber() != null ? s.sequenceNumber() : 0);
                        sRow.createCell(1).setCellValue(s.trackingNumber() != null ? s.trackingNumber() : "");
                        sRow.createCell(2).setCellValue(s.senderFullName() != null ? s.senderFullName() : "");
                        sRow.createCell(3).setCellValue(s.recipientFullName() != null ? s.recipientFullName() : "");
                        sRow.createCell(4).setCellValue(s.originCityName() != null ? s.originCityName() : "");
                        sRow.createCell(5).setCellValue(s.destinationCityName() != null ? s.destinationCityName() : "");
                        sRow.createCell(6).setCellValue(s.actualWeight() != null ? s.actualWeight().doubleValue() : 0);
                        sRow.createCell(7).setCellValue(s.totalPrice() != null ? s.totalPrice().doubleValue() : 0);
                        sRow.createCell(8).setCellValue(s.shipmentStatusName() != null ? s.shipmentStatusName() : "");
                    }

                    sheet.createRow(rowIdx++);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=waybills.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    @Override
    public String getFormat() { return "xlsx"; }
}