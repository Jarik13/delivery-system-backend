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
import org.deliverysystem.com.dtos.route_lists.RouteListDto;
import org.deliverysystem.com.dtos.route_lists.RouteSheetItemDto;
import org.deliverysystem.com.export.RouteListExportStrategy;
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
public class PdfRouteListExportStrategy implements RouteListExportStrategy {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DeviceRgb COLOR_HEADER_BG = new DeviceRgb(103, 58, 183);
    private static final DeviceRgb COLOR_RL_BG = new DeviceRgb(237, 231, 246);
    private static final DeviceRgb COLOR_ROW_ODD = new DeviceRgb(248, 245, 255);
    private static final DeviceRgb COLOR_BORDER = new DeviceRgb(206, 195, 230);
    private static final DeviceRgb COLOR_GREY_TEXT = new DeviceRgb(100, 100, 100);
    private static final DeviceRgb COLOR_BODY_TEXT = new DeviceRgb(40, 40, 40);
    private static final DeviceRgb COLOR_RL_TEXT = new DeviceRgb(69, 39, 160);
    private static final DeviceRgb COLOR_WHITE = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb COLOR_GREEN = new DeviceRgb(46, 125, 50);
    private static final DeviceRgb COLOR_ORANGE = new DeviceRgb(230, 81, 0);

    @Override
    public String getFormat() {
        return "pdf";
    }

    @Override
    public ResponseEntity<byte[]> export(List<RouteListDto> routeLists) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfFont font = loadFont();
            PdfFont fontBold = loadFontBold();

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterEventHandler(font));

            Document document = new Document(pdfDoc);
            document.setMargins(40, 30, 40, 30);
            document.setFont(font);

            addTitle(document, routeLists, font, fontBold);
            document.add(new Paragraph("\n").setMarginBottom(4));
            document.add(buildTable(routeLists, font, fontBold));
            document.close();

            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"route-lists.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(bytes.length)
                    .body(bytes);

        } catch (Exception e) {
            throw new RuntimeException("Помилка генерації PDF: " + e.getMessage(), e);
        }
    }

    private void addTitle(Document document, List<RouteListDto> routeLists,
                          PdfFont font, PdfFont fontBold) {
        int totalShipments = routeLists.stream()
                .mapToInt(r -> r.items() == null ? 0 : r.items().size())
                .sum();

        document.add(new Paragraph("Маршрутні листи")
                .setFont(fontBold).setFontSize(18)
                .setFontColor(COLOR_HEADER_BG).setMarginBottom(4));

        document.add(new Paragraph(
                "Листів: " + routeLists.size() +
                "   |   Відправлень: " + totalShipments +
                "   |   Сформовано: " + LocalDateTime.now().format(DATE_FMT))
                .setFont(font).setFontSize(9)
                .setFontColor(COLOR_GREY_TEXT).setMarginBottom(2));
    }

    private Table buildTable(List<RouteListDto> routeLists, PdfFont font, PdfFont fontBold) {
        float[] widths = {2f, 5f, 7f, 6f, 5f, 5f, 4f, 5f, 5f, 4f};
        Table table = new Table(UnitValue.createPercentArray(widths))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(6);

        String[] headers = {"№", "Трек-номер", "Отримувач", "Адреса доставки",
                "Телефон", "Вага (кг)", "ЦОД (грн)", "Статус", "Час вручення", "Доставлено"};
        for (String h : headers) table.addHeaderCell(headerCell(h, fontBold));

        int numCols = widths.length;
        int rowIdx = 0;

        for (RouteListDto rl : routeLists) {
            String rlText =
                    "Лист ML-" + str(rl.number()) +
                    "   |   Кур'єр: " + str(rl.courierFullName()) +
                    "   |   Статус: " + str(rl.statusName()) +
                    "   |   Вага: " + formatDecimal(rl.totalWeight()) + " кг" +
                    "   |   Виїзд: " + formatDate(rl.plannedDepartureTime()) +
                    "   |   Створено: " + formatDate(rl.createdAt());

            table.addCell(new Cell(1, numCols)
                    .add(new Paragraph(rlText).setFont(fontBold).setFontSize(8).setFontColor(COLOR_RL_TEXT))
                    .setBackgroundColor(COLOR_RL_BG)
                    .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                    .setPadding(5f));

            List<RouteSheetItemDto> items = rl.items();
            if (items == null || items.isEmpty()) {
                table.addCell(new Cell(1, numCols)
                        .add(new Paragraph("— відправлень немає —")
                                .setFont(font).setFontSize(7)
                                .setFontColor(COLOR_GREY_TEXT)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setBorder(new SolidBorder(COLOR_BORDER, 0.5f)).setPadding(4f));
                continue;
            }

            int seq = 1;
            for (RouteSheetItemDto s : items) {
                DeviceRgb bg = (rowIdx++ % 2 == 0) ? null : COLOR_ROW_ODD;
                DeviceRgb statusColor = s.isDelivered() ? COLOR_GREEN : COLOR_ORANGE;
                String statusText = s.isDelivered() ? "Доставлено" : "В процесі";

                table.addCell(dataCell(String.valueOf(seq++), font, fontBold, bg, TextAlignment.CENTER, true));
                table.addCell(dataCell(str(s.trackingNumber()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.recipientFullName()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.deliveryAddress()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(str(s.recipientPhone()), font, fontBold, bg, TextAlignment.LEFT, false));
                table.addCell(dataCell(formatDecimal(s.weight()), font, fontBold, bg, TextAlignment.RIGHT, false));
                table.addCell(dataCell(formatDecimal(s.codAmount()), font, fontBold, bg, TextAlignment.RIGHT, false));
                table.addCell(coloredCell(statusText, font, bg, statusColor));
                table.addCell(dataCell(formatDate(s.deliveredAt()), font, fontBold, bg, TextAlignment.CENTER, false));
                table.addCell(dataCell(s.isDelivered() ? "✓" : "—", font, fontBold, bg, TextAlignment.CENTER, s.isDelivered()));
            }
        }
        return table;
    }

    private Cell headerCell(String text, PdfFont fontBold) {
        return new Cell()
                .add(new Paragraph(text).setFont(fontBold).setFontSize(8)
                        .setFontColor(COLOR_WHITE).setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(COLOR_HEADER_BG)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f)).setPadding(5f);
    }

    private Cell dataCell(String text, PdfFont font, PdfFont fontBold,
                          DeviceRgb bg, TextAlignment align, boolean bold) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(bold ? fontBold : font).setFontSize(7)
                        .setFontColor(COLOR_BODY_TEXT).setTextAlignment(align))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f)).setPadding(4f);
        if (bg != null) cell.setBackgroundColor(bg);
        return cell;
    }

    private Cell coloredCell(String text, PdfFont font, DeviceRgb bg, DeviceRgb textColor) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setFont(font).setFontSize(7)
                        .setFontColor(textColor).setTextAlignment(TextAlignment.CENTER))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f)).setPadding(4f);
        if (bg != null) cell.setBackgroundColor(bg);
        return cell;
    }

    private PdfFont loadFont() throws IOException {
        return loadFontFromSources("DejaVuSans.ttf", "arial.ttf", "FreeSans.ttf");
    }

    private PdfFont loadFontBold() throws IOException {
        try {
            return loadFontFromSources("DejaVuSans-Bold.ttf", "arialbd.ttf", "FreeSansBold.ttf");
        } catch (Exception e) {
            return loadFont();
        }
    }

    private PdfFont loadFontFromSources(String classpathFile, String winFile, String linuxFile)
            throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/fonts/" + classpathFile)) {
            if (is != null) return PdfFontFactory.createFont(
                    is.readAllBytes(), PdfEncodings.IDENTITY_H,
                    PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("C:/Windows/Fonts/" + winFile,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("/usr/share/fonts/truetype/freefont/" + linuxFile,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("/usr/share/fonts/truetype/dejavu/" + classpathFile,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

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
            pdfCanvas.setStrokeColor(new DeviceRgb(200, 200, 200))
                    .setLineWidth(0.5f)
                    .moveTo(30, 28).lineTo(pageSize.getWidth() - 30, 28).stroke();
            String footerText = "Сторінка " + pageNumber +
                                "   |   Система управління доставкою — документ згенеровано автоматично";
            try (Canvas canvas = new Canvas(pdfCanvas, pageSize)) {
                canvas.setFont(font).setFontSize(7)
                        .setFontColor(new DeviceRgb(130, 130, 130))
                        .showTextAligned(new Paragraph(footerText),
                                pageSize.getWidth() / 2f, 17f, pageNumber,
                                TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
            }
            pdfCanvas.release();
        }
    }
}