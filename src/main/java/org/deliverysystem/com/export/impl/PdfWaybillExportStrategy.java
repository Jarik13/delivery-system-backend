package org.deliverysystem.com.export.impl;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.export.WaybillExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfWaybillExportStrategy implements WaybillExportStrategy {
    private static final DateTimeFormatter DATE_FMT      = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final Color COLOR_HEADER_BG    = new Color(30, 100, 180);
    private static final Color COLOR_WAYBILL_BG   = new Color(220, 235, 255);
    private static final Color COLOR_ROW_ODD      = new Color(245, 248, 255);
    private static final Color COLOR_ROW_EVEN     = Color.WHITE;
    private static final Color COLOR_BORDER       = new Color(200, 215, 235);
    private static final Color COLOR_GREY_TEXT    = new Color(100, 100, 100);
    private static final Color COLOR_BODY_TEXT    = new Color(40, 40, 40);
    private static final Color COLOR_WAYBILL_TEXT = new Color(20, 70, 140);

    @Override
    public ResponseEntity<byte[]> export(List<WaybillDto> waybills) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate(), 30, 30, 40, 30);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.setPageEvent(new HeaderFooterEvent());
            document.open();

            addTitle(document, waybills);
            document.add(new Paragraph(" "));
            document.add(buildTable(waybills));

            document.close();

            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"waybills.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(bytes.length)
                    .body(bytes);

        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації PDF: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFormat() {
        return "pdf";
    }

    private void addTitle(Document document, List<WaybillDto> waybills) throws DocumentException {
        BaseFont bf = loadBaseFont();
        Font titleFont = new Font(bf, 18, Font.BOLD, COLOR_HEADER_BG);
        Font subFont   = new Font(bf, 10, Font.NORMAL, COLOR_GREY_TEXT);

        int totalShipments = waybills.stream()
                .mapToInt(w -> w.shipments() == null ? 0 : w.shipments().size())
                .sum();

        Paragraph title = new Paragraph("Транспортні накладні", titleFont);
        title.setAlignment(Element.ALIGN_LEFT);
        document.add(title);

        Paragraph sub = new Paragraph(
                "Накладних: " + waybills.size() +
                "   |   Відправлень: " + totalShipments +
                "   |   Сформовано: " + LocalDateTime.now().format(DATE_FMT),
                subFont);
        sub.setAlignment(Element.ALIGN_LEFT);
        document.add(sub);
    }

    private PdfPTable buildTable(List<WaybillDto> waybills) throws DocumentException {
        float[] widths = {3f, 9f, 8f, 8f, 7f, 7f, 4f, 5f, 6f, 7f};
        PdfPTable table = new PdfPTable(widths);
        table.setWidthPercentage(100);
        table.setSpacingBefore(6);

        int numCols = widths.length;
        BaseFont bf = loadBaseFont();

        Font headerFont   = new Font(bf, 8, Font.BOLD,   Color.WHITE);
        Font waybillFont  = new Font(bf, 8, Font.BOLD,   COLOR_WAYBILL_TEXT);
        Font bodyFont     = new Font(bf, 7, Font.NORMAL,  COLOR_BODY_TEXT);
        Font boldFont     = new Font(bf, 7, Font.BOLD,    COLOR_BODY_TEXT);

        String[] headers = {"№ п/п", "Трек-номер", "Відправник", "Отримувач",
                "Звідки", "Куди", "Вага (кг)", "Ціна", "Статус", "Дата"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(COLOR_HEADER_BG);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setPadding(5f);
            cell.setBorderColor(COLOR_BORDER);
            cell.setBorderWidth(0.5f);
            table.addCell(cell);
        }
        table.setHeaderRows(1);

        int shipmentRowIdx = 0;
        for (WaybillDto w : waybills) {
            String waybillHeader = "Накладна № " + str(w.number()) +
                                   "   |   Створив: " + str(w.createdByName()) +
                                   "   |   Відправлень: " + (w.shipmentsCount() != null ? w.shipmentsCount() : 0) +
                                   "   |   Загальна вага: " + formatDecimal(w.totalWeight()) + " кг" +
                                   "   |   Об'єм: " + formatDecimal(w.volume()) + " м³" +
                                   "   |   Дата: " + formatDate(w.createdAt());
            PdfPCell waybillCell = new PdfPCell(new Phrase(waybillHeader, waybillFont));
            waybillCell.setColspan(numCols);
            waybillCell.setBackgroundColor(COLOR_WAYBILL_BG);
            waybillCell.setPadding(5f);
            waybillCell.setBorderColor(COLOR_BORDER);
            waybillCell.setBorderWidth(0.5f);
            table.addCell(waybillCell);

            List<WaybillShipmentDto> shipments = w.shipments();
            if (shipments == null || shipments.isEmpty()) {
                PdfPCell empty = new PdfPCell(new Phrase("— відправлень немає —", bodyFont));
                empty.setColspan(numCols);
                empty.setHorizontalAlignment(Element.ALIGN_CENTER);
                empty.setPadding(4f);
                empty.setBorderColor(COLOR_BORDER);
                empty.setBorderWidth(0.5f);
                table.addCell(empty);
                continue;
            }

            for (WaybillShipmentDto s : shipments) {
                Color bg = (shipmentRowIdx % 2 == 0) ? COLOR_ROW_EVEN : COLOR_ROW_ODD;
                shipmentRowIdx++;

                addCell(table, str(s.sequenceNumber()),      boldFont, bg, Element.ALIGN_CENTER);
                addCell(table, str(s.trackingNumber()),      bodyFont, bg, Element.ALIGN_LEFT);
                addCell(table, str(s.senderFullName()),      bodyFont, bg, Element.ALIGN_LEFT);
                addCell(table, str(s.recipientFullName()),   bodyFont, bg, Element.ALIGN_LEFT);
                addCell(table, str(s.originCityName()),      bodyFont, bg, Element.ALIGN_LEFT);
                addCell(table, str(s.destinationCityName()), bodyFont, bg, Element.ALIGN_LEFT);
                addCell(table, formatDecimal(s.actualWeight()), bodyFont, bg, Element.ALIGN_RIGHT);
                addCell(table, formatDecimal(s.totalPrice()),   bodyFont, bg, Element.ALIGN_RIGHT);
                addCell(table, str(s.shipmentStatusName()),  bodyFont, bg, Element.ALIGN_CENTER);
                addCell(table, formatDate(s.createdAt()),    bodyFont, bg, Element.ALIGN_CENTER);
            }
        }

        return table;
    }

    private void addCell(PdfPTable table, String text, Font font, Color bg, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(bg);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(4f);
        cell.setBorderColor(COLOR_BORDER);
        cell.setBorderWidth(0.5f);
        table.addCell(cell);
    }

    private BaseFont loadBaseFont() {
        try {
            return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            throw new RuntimeException("Не вдалось завантажити шрифт PDF", e);
        }
    }

    private String str(Object val) {
        return val == null ? "—" : val.toString().trim();
    }

    private String formatDecimal(Object val) {
        if (val == null) return "—";
        try {
            return String.format("%.2f", Double.parseDouble(val.toString()));
        } catch (NumberFormatException e) {
            return val.toString();
        }
    }

    private String formatDate(Object val) {
        return switch (val) {
            case null                -> "—";
            case LocalDateTime ldt   -> ldt.format(DATE_FMT);
            case LocalDate ld        -> ld.format(DATE_ONLY_FMT);
            default                  -> val.toString();
        };
    }

    private static class HeaderFooterEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                PdfContentByte cb = writer.getDirectContent();
                Rectangle page = document.getPageSize();

                cb.setColorStroke(new Color(200, 200, 200));
                cb.setLineWidth(0.5f);
                cb.moveTo(30, 28);
                cb.lineTo(page.getWidth() - 30, 28);
                cb.stroke();

                cb.setColorFill(new Color(130, 130, 130));
                cb.setFontAndSize(bf, 7);
                cb.beginText();
                cb.showTextAligned(
                        PdfContentByte.ALIGN_CENTER,
                        "Сторінка " + writer.getPageNumber() +
                        "   |   Система управління доставкою — документ згенеровано автоматично",
                        page.getWidth() / 2f, 17f, 0);
                cb.endText();

            } catch (Exception ignored) {}
        }
    }
}