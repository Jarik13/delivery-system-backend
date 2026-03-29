package org.deliverysystem.com.export.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.export.ShipmentExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ExcelShipmentExportStrategy implements ShipmentExportStrategy {
    @Override
    public String getFormat() {
        return "xlsx";
    }

    @Override
    public ResponseEntity<byte[]> export(List<ShipmentDto> shipments) {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Відправлення");
            sheet.setDefaultColumnWidth(18);

            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 10);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle oddStyle = wb.createCellStyle();
            oddStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            oddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle numStyle = wb.createCellStyle();
            DataFormat fmt = wb.createDataFormat();
            numStyle.setDataFormat(fmt.getFormat("#,##0.00"));

            CellStyle numOddStyle = wb.createCellStyle();
            numOddStyle.cloneStyleFrom(numStyle);
            numOddStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            numOddStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            String[] headers = {
                    "Трек-номер", "Відправник", "Отримувач",
                    "Місто відправлення", "Пункт відправлення",
                    "Місто призначення", "Пункт призначення",
                    "Тип доставки", "Статус",
                    "Вага (кг)", "Оголошена вартість",
                    "Доставка", "За вагу", "Відстань",
                    "Коробка", "Спец. пакування", "Страховка", "Всього",
                    "Оплачено", "Залишок", "Оплата відправником",
                    "Накладна", "Маршрутний лист",
                    "Дата створення", "Дата видачі"
            };

            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(22);
            for (int i = 0; i < headers.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(headers[i]);
                c.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (ShipmentDto s : shipments) {
                Row row = sheet.createRow(rowIdx);
                boolean odd = rowIdx % 2 != 0;
                CellStyle base = odd ? oddStyle : null;
                CellStyle num = odd ? numOddStyle : numStyle;

                setStr(row, 0, s.trackingNumber(), base);
                setStr(row, 1, s.senderFullName(), base);
                setStr(row, 2, s.recipientFullName(), base);
                setStr(row, 3, s.originCityName(), base);
                setStr(row, 4, s.originLocationName(), base);
                setStr(row, 5, s.destinationCityName(), base);
                setStr(row, 6, s.destinationLocationName(), base);
                setStr(row, 7, s.shipmentTypeName(), base);
                setStr(row, 8, s.shipmentStatusName(), base);
                setNum(row, 9, s.actualWeight(), num);
                setNum(row, 10, s.declaredValue(), num);
                setNum(row, 11, s.deliveryPrice(), num);
                setNum(row, 12, s.weightPrice(), num);
                setNum(row, 13, s.distancePrice(), num);
                setNum(row, 14, s.boxVariantPrice(), num);
                setNum(row, 15, s.specialPackagingPrice(), num);
                setNum(row, 16, s.insuranceFee(), num);
                setNum(row, 17, s.totalPrice(), num);
                setNum(row, 18, s.totalPaidAmount(), num);
                setNum(row, 19, s.remainingAmount(), num);
                setStr(row, 20, s.isSenderPay() != null
                        ? (s.isSenderPay() ? "Відправник" : "Отримувач") : "", base);
                setStr(row, 21, s.waybillNumber(), base);
                setStr(row, 22, s.routeListNumber(), base);
                setStr(row, 23, s.createdAt() != null ? s.createdAt().toString() : "", base);
                setStr(row, 24, s.issuedAt() != null ? s.issuedAt().toString() : "", base);

                rowIdx++;
            }

            for (int i = 0; i < 9; i++) sheet.autoSizeColumn(i);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            wb.write(out);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=shipments.xlsx")
                    .contentType(MediaType.parseMediaType(
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Excel export failed", e);
        }
    }

    private void setStr(Row row, int col, Object val, CellStyle style) {
        Cell c = row.createCell(col);
        c.setCellValue(val != null ? val.toString() : "");
        if (style != null) c.setCellStyle(style);
    }

    private void setNum(Row row, int col, BigDecimal val, CellStyle style) {
        Cell c = row.createCell(col);
        if (val != null) {
            c.setCellValue(val.doubleValue());
        } else {
            c.setCellValue(0.0);
        }
        if (style != null) c.setCellStyle(style);
    }
}