package org.deliverysystem.com.export.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteSheetItemDto;
import org.deliverysystem.com.export.RouteListExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelRouteListExportStrategy implements RouteListExportStrategy {
    @Override
    public String getFormat() { return "xlsx"; }

    @Override
    public ResponseEntity<byte[]> export(List<RouteListDto> routeLists) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Маршрутні листи");
            sheet.setDefaultColumnWidth(20);

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle rlStyle = wb.createCellStyle();
            Font rlFont = wb.createFont();
            rlFont.setBold(true);
            rlStyle.setFont(rlFont);
            rlStyle.setFillForegroundColor(IndexedColors.LAVENDER.getIndex());
            rlStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int rowIdx = 0;

            Row header = sheet.createRow(rowIdx++);
            String[] cols = {"Номер листа", "Кур'єр", "Статус", "Вага (кг)", "Плановий виїзд", "Дата створення"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            for (RouteListDto rl : routeLists) {
                Row rlRow = sheet.createRow(rowIdx++);
                rlRow.createCell(0).setCellValue("ML-" + (rl.number() != null ? rl.number() : ""));
                rlRow.createCell(1).setCellValue(rl.courierFullName() != null ? rl.courierFullName() : "");
                rlRow.createCell(2).setCellValue(rl.statusName() != null ? rl.statusName() : "");
                rlRow.createCell(3).setCellValue(rl.totalWeight() != null ? rl.totalWeight().doubleValue() : 0);
                rlRow.createCell(4).setCellValue(rl.plannedDepartureTime() != null ? rl.plannedDepartureTime().toString() : "");
                rlRow.createCell(5).setCellValue(rl.createdAt() != null ? rl.createdAt().toString() : "");
                for (int i = 0; i < 6; i++) rlRow.getCell(i).setCellStyle(rlStyle);

                if (rl.items() != null && !rl.items().isEmpty()) {
                    Row itemHeader = sheet.createRow(rowIdx++);
                    String[] iCols = {"#", "Трек-номер", "Отримувач", "Адреса", "Телефон", "Вага", "ЦОД (грн)", "Статус", "Час вручення"};
                    for (int i = 0; i < iCols.length; i++) {
                        Cell c = itemHeader.createCell(i);
                        c.setCellValue(iCols[i]);
                        c.setCellStyle(headerStyle);
                    }

                    int seq = 1;
                    for (RouteSheetItemDto s : rl.items()) {
                        Row sRow = sheet.createRow(rowIdx++);
                        sRow.createCell(0).setCellValue(seq++);
                        sRow.createCell(1).setCellValue(s.trackingNumber() != null ? s.trackingNumber() : "");
                        sRow.createCell(2).setCellValue(s.recipientFullName() != null ? s.recipientFullName() : "");
                        sRow.createCell(3).setCellValue(s.deliveryAddress() != null ? s.deliveryAddress() : "");
                        sRow.createCell(4).setCellValue(s.recipientPhone() != null ? s.recipientPhone() : "");
                        sRow.createCell(5).setCellValue(s.weight() != null ? s.weight().doubleValue() : 0);
                        sRow.createCell(6).setCellValue(s.codAmount() != null ? s.codAmount().doubleValue() : 0);
                        sRow.createCell(7).setCellValue(s.isDelivered() ? "Доставлено" : "В процесі");
                        sRow.createCell(8).setCellValue(s.deliveredAt() != null ? s.deliveredAt().toString() : "");
                    }

                    sheet.createRow(rowIdx++);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=route-lists.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }
}