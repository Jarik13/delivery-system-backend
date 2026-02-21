package org.deliverysystem.com.export.impl;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.deliverysystem.com.dtos.waybills.WaybillDto;
import org.deliverysystem.com.dtos.waybills.WaybillShipmentDto;
import org.deliverysystem.com.export.WaybillExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfWaybillExportStrategy implements WaybillExportStrategy {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    // Кольори (DeviceRgb для iText 7/8)
    private static final DeviceRgb COLOR_HEADER_BG = new DeviceRgb(30, 100, 180);
    private static final DeviceRgb COLOR_WAYBILL_BG = new DeviceRgb(220, 235, 255);
    private static final DeviceRgb COLOR_ROW_ODD = new DeviceRgb(245, 248, 255);
    private static final DeviceRgb COLOR_BORDER = new DeviceRgb(200, 215, 235);
    private static final DeviceRgb COLOR_GREY_TEXT = new DeviceRgb(100, 100, 100);
    private static final DeviceRgb COLOR_BODY_TEXT = new DeviceRgb(40, 40, 40);
    private static final DeviceRgb COLOR_WAYBILL_TEXT = new DeviceRgb(20, 70, 140);
    private static final DeviceRgb COLOR_WHITE = new DeviceRgb(255, 255, 255);

    @Override
    public String getFormat() {
        return "pdf";
    }

    @Override
    public ResponseEntity<byte[]> export(List<WaybillDto> waybills) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfFont font = loadFont();
            PdfFont fontBold = loadFontBold();

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());

            // Додаємо колонтитул як event handler
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE,
                    new FooterEventHandler(font));

            Document document = new Document(pdfDoc);
            document.setMargins(40, 30, 40, 30);
            document.setFont(font);

            addTitle(document, waybills, font, fontBold);
            document.add(new Paragraph("\n").setMarginBottom(4));
            document.add(buildTable(waybills, font, fontBold));

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

    // ── Заголовок документа ───────────────────────────────────────────────────

    private void addTitle(Document document, List<WaybillDto> waybills,
                          PdfFont font, PdfFont fontBold) {
        int totalShipments = waybills.stream()
                .mapToInt(w -> w.shipments() == null ? 0 : w.shipments().size())
                .sum();

        document.add(new Paragraph("Транспортні накладні")
                .setFont(fontBold)
                .setFontSize(18)
                .setFontColor(COLOR_HEADER_BG)
                .setMarginBottom(4));

        document.add(new Paragraph(
                "Накладних: " + waybills.size() +
                "   |   Відправлень: " + totalShipments +
                "   |   Сформовано: " + LocalDateTime.now().format(DATE_FMT))
                .setFont(font)
                .setFontSize(9)
                .setFontColor(COLOR_GREY_TEXT)
                .setMarginBottom(2));
    }

    // ── Таблиця ───────────────────────────────────────────────────────────────

    private Table buildTable(List<WaybillDto> waybills, PdfFont font, PdfFont fontBold) {
        // Відносні ширини колонок
        float[] widths = {3f, 9f, 8f, 8f, 7f, 7f, 4f, 5f, 6f, 7f};
        Table table = new Table(UnitValue.createPercentArray(widths))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(6);

        // Заголовки
        String[] headers = {"№ п/п", "Трек-номер", "Відправник", "Отримувач",
                "Звідки", "Куди", "Вага (кг)", "Ціна", "Статус", "Дата"};
        for (String h : headers) {
            table.addHeaderCell(headerCell(h, fontBold));
        }

        int numCols = widths.length;
        int shipmentRowIdx = 0;

        for (WaybillDto w : waybills) {

            // Рядок-заголовок накладної
            String waybillText =
                    "Накладна \u2116 " + str(w.number()) +
                    "   |   Створив: " + str(w.createdByName()) +
                    "   |   Відправлень: " + (w.shipmentsCount() != null ? w.shipmentsCount() : 0) +
                    "   |   Загальна вага: " + formatDecimal(w.totalWeight()) + " кг" +
                    "   |   Об'єм: " + formatDecimal(w.volume()) + " м\u00B3" +
                    "   |   Дата: " + formatDate(w.createdAt());

            Cell waybillCell = new Cell(1, numCols)
                    .add(new Paragraph(waybillText)
                            .setFont(fontBold)
                            .setFontSize(8)
                            .setFontColor(COLOR_WAYBILL_TEXT))
                    .setBackgroundColor(COLOR_WAYBILL_BG)
                    .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                    .setPadding(5f);
            table.addCell(waybillCell);

            List<WaybillShipmentDto> shipments = w.shipments();
            if (shipments == null || shipments.isEmpty()) {
                Cell empty = new Cell(1, numCols)
                        .add(new Paragraph("\u2014 відправлень немає \u2014")
                                .setFont(font).setFontSize(7)
                                .setFontColor(COLOR_GREY_TEXT)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                        .setPadding(4f);
                table.addCell(empty);
                continue;
            }

            for (WaybillShipmentDto s : shipments) {
                DeviceRgb bg = (shipmentRowIdx % 2 == 0) ? null : COLOR_ROW_ODD;
                shipmentRowIdx++;

                table.addCell(dataCell(str(s.sequenceNumber()), font, fontBold, bg, TextAlignment.CENTER, true));
                table.addCell(dataCell(str(s.trackingNumber()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.senderFullName()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.recipientFullName()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.originCityName()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.destinationCityName()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(formatDecimal(s.actualWeight()), font, fontBold, bg, TextAlignment.RIGHT, false));
                table.addCell(dataCell(formatDecimal(s.totalPrice()), font, fontBold, bg, TextAlignment.RIGHT, false));
                table.addCell(dataCell(str(s.shipmentStatusName()), font, fontBold, bg, TextAlignment.CENTER, false));
                table.addCell(dataCell(formatDate(s.createdAt()), font, fontBold, bg, TextAlignment.CENTER, false));
            }
        }

        return table;
    }

    // ── Фабричні методи для клітинок ─────────────────────────────────────────

    private Cell headerCell(String text, PdfFont fontBold) {
        return new Cell()
                .add(new Paragraph(text)
                        .setFont(fontBold)
                        .setFontSize(8)
                        .setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(COLOR_HEADER_BG)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                .setPadding(5f);
    }

    private Cell dataCell(String text, PdfFont font, PdfFont fontBold,
                          DeviceRgb bg, TextAlignment align, boolean bold) {
        Cell cell = new Cell()
                .add(new Paragraph(text)
                        .setFont(bold ? fontBold : font)
                        .setFontSize(7)
                        .setFontColor(COLOR_BODY_TEXT)
                        .setTextAlignment(align))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                .setPadding(4f);
        if (bg != null) cell.setBackgroundColor(bg);
        return cell;
    }

    // ── Шрифт ────────────────────────────────────────────────────────────────

    /**
     * Завантажує regular-шрифт з підтримкою кирилиці.
     * Порядок: classpath /fonts/DejaVuSans.ttf → Arial (Win) → FreeSans (Linux) → fallback.
     */
    private PdfFont loadFont() throws IOException {
        return loadFontFromSources("DejaVuSans.ttf", "arial.ttf", "FreeSans.ttf");
    }

    /**
     * Завантажує bold-шрифт.
     * Якщо окремого bold немає — повертає той самий шрифт (iText сам імітує bold).
     */
    private PdfFont loadFontBold() throws IOException {
        try {
            return loadFontFromSources("DejaVuSans-Bold.ttf", "arialbd.ttf", "FreeSansBold.ttf");
        } catch (Exception e) {
            return loadFont(); // fallback — той самий шрифт
        }
    }

    private PdfFont loadFontFromSources(String classpathFile, String winFile, String linuxFile)
            throws IOException {

        // 1. Classpath (src/main/resources/fonts/<file>)
        try (InputStream is = getClass().getResourceAsStream("/fonts/" + classpathFile)) {
            if (is != null) {
                return PdfFontFactory.createFont(
                        is.readAllBytes(), PdfEncodings.IDENTITY_H,
                        PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
            }
        } catch (Exception ignored) {
        }

        // 2. Windows
        try {
            return PdfFontFactory.createFont(
                    "C:/Windows/Fonts/" + winFile, PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }

        // 3. Linux FreeFonts
        try {
            return PdfFontFactory.createFont(
                    "/usr/share/fonts/truetype/freefont/" + linuxFile, PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }

        // 4. Linux DejaVu
        try {
            return PdfFontFactory.createFont(
                    "/usr/share/fonts/truetype/dejavu/" + classpathFile, PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }

        // Fallback — Helvetica без кирилиці
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

    // ── Хелпери ───────────────────────────────────────────────────────────────

    private String str(Object val) {
        return val == null ? "\u2014" : val.toString().trim();
    }

    private String formatDecimal(Object val) {
        if (val == null) return "\u2014";
        try {
            return String.format("%.2f", Double.parseDouble(val.toString()));
        } catch (NumberFormatException e) {
            return val.toString();
        }
    }

    private String formatDate(Object val) {
        return switch (val) {
            case null -> "\u2014";
            case LocalDateTime ldt -> ldt.format(DATE_FMT);
            case LocalDate ld -> ld.format(DATE_ONLY_FMT);
            default -> val.toString();
        };
    }

    private record FooterEventHandler(PdfFont font) implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdfDoc.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();

            PdfCanvas pdfCanvas = new PdfCanvas(page);

            // Лінія-роздільник
            pdfCanvas.setStrokeColor(new DeviceRgb(200, 200, 200))
                    .setLineWidth(0.5f)
                    .moveTo(30, 28)
                    .lineTo(pageSize.getWidth() - 30, 28)
                    .stroke();

            // Текст колонтитула
            String footerText = "Сторінка " + pageNumber + "   |   Система управління доставкою \u2014 документ згенеровано автоматично";

            try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
                canvas.setFont(font)
                        .setFontSize(7)
                        .setFontColor(new DeviceRgb(130, 130, 130))
                        .showTextAligned(
                                new Paragraph(footerText),
                                pageSize.getWidth() / 2f, 17f,
                                pageNumber,
                                TextAlignment.CENTER,
                                VerticalAlignment.BOTTOM,
                                0);
            }

            pdfCanvas.release();
        }
    }
}