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
import org.deliverysystem.com.dtos.shipments.ShipmentDto;
import org.deliverysystem.com.export.ShipmentExportStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfShipmentExportStrategy implements ShipmentExportStrategy {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final DateTimeFormatter DATE_ONLY_FMT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final DeviceRgb COLOR_HEADER_BG = new DeviceRgb(30, 100, 180);
    private static final DeviceRgb COLOR_ROW_ODD = new DeviceRgb(240, 246, 255);
    private static final DeviceRgb COLOR_BORDER = new DeviceRgb(200, 215, 235);
    private static final DeviceRgb COLOR_GREY_TEXT = new DeviceRgb(100, 100, 100);
    private static final DeviceRgb COLOR_BODY_TEXT = new DeviceRgb(40, 40, 40);
    private static final DeviceRgb COLOR_WHITE = new DeviceRgb(255, 255, 255);

    @Override
    public String getFormat() {
        return "pdf";
    }

    @Override
    public ResponseEntity<byte[]> export(List<ShipmentDto> shipments) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfFont font = loadFont();
            PdfFont fontBold = loadFontBold();

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            pdfDoc.setDefaultPageSize(PageSize.A4.rotate());
            pdfDoc.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterHandler(font));

            Document document = new Document(pdfDoc);
            document.setMargins(40, 30, 40, 30);
            document.setFont(font);

            document.add(new Paragraph("Відправлення")
                    .setFont(fontBold).setFontSize(18)
                    .setFontColor(COLOR_HEADER_BG).setMarginBottom(4));

            document.add(new Paragraph(
                    "Записів: " + shipments.size() +
                    "   |   Сформовано: " + LocalDateTime.now().format(DATE_FMT))
                    .setFont(font).setFontSize(9)
                    .setFontColor(COLOR_GREY_TEXT).setMarginBottom(8));

            document.add(buildTable(shipments, font, fontBold));
            document.close();

            byte[] bytes = baos.toByteArray();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"shipments.pdf\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(bytes.length)
                    .body(bytes);

        } catch (Exception e) {
            throw new RuntimeException("PDF export failed: " + e.getMessage(), e);
        }
    }

    private Table buildTable(List<ShipmentDto> shipments, PdfFont font, PdfFont fontBold) {
        float[] widths = {5f, 7f, 7f, 5f, 6f, 5f, 6f, 5f, 6f, 3.5f, 4f, 4f, 5f, 5f};
        Table table = new Table(UnitValue.createPercentArray(widths))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginTop(4);

        String[] headers = {
                "Трек-номер", "Відправник", "Отримувач",
                "Місто відправ.", "Пункт відправ.",
                "Місто признач.", "Пункт признач.",
                "Тип", "Статус",
                "Вага (кг)", "Всього (₴)", "Оплачено (₴)", "Дата створення", "Дата видачі"
        };
        for (String h : headers) table.addHeaderCell(headerCell(h, fontBold));

        int idx = 0;
        for (ShipmentDto s : shipments) {
            DeviceRgb bg = (idx % 2 != 0) ? COLOR_ROW_ODD : null;
            table.addCell(dataCell(s.trackingNumber(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.senderFullName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.recipientFullName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.originCityName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.originLocationName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.destinationCityName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.destinationLocationName(), font, bg, TextAlignment.LEFT));
            table.addCell(dataCell(s.shipmentTypeName(), font, bg, TextAlignment.CENTER));
            table.addCell(dataCell(s.shipmentStatusName(), font, bg, TextAlignment.CENTER));
            table.addCell(dataCell(fmtNum(s.actualWeight()), font, bg, TextAlignment.RIGHT));
            table.addCell(dataCell(fmtNum(s.totalPrice()), font, bg, TextAlignment.RIGHT));
            table.addCell(dataCell(fmtNum(s.totalPaidAmount()), font, bg, TextAlignment.RIGHT));
            table.addCell(dataCell(fmtDate(s.createdAt()), font, bg, TextAlignment.CENTER));
            table.addCell(dataCell(fmtDate(s.issuedAt()), font, bg, TextAlignment.CENTER));
            idx++;
        }
        return table;
    }

    private Cell headerCell(String text, PdfFont fontBold) {
        return new Cell()
                .add(new Paragraph(text).setFont(fontBold).setFontSize(7)
                        .setFontColor(COLOR_WHITE).setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(COLOR_HEADER_BG)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                .setPadding(4f);
    }

    private Cell dataCell(String text, PdfFont font, DeviceRgb bg, TextAlignment align) {
        Cell c = new Cell()
                .add(new Paragraph(text == null ? "—" : text)
                        .setFont(font).setFontSize(6.5f)
                        .setFontColor(COLOR_BODY_TEXT).setTextAlignment(align))
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setBorder(new SolidBorder(COLOR_BORDER, 0.5f))
                .setPadding(3.5f);
        if (bg != null) c.setBackgroundColor(bg);
        return c;
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

    private PdfFont loadFontFromSources(String cp, String win, String linux) throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/fonts/" + cp)) {
            if (is != null)
                return PdfFontFactory.createFont(is.readAllBytes(), PdfEncodings.IDENTITY_H,
                        PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("C:/Windows/Fonts/" + win,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("/usr/share/fonts/truetype/freefont/" + linux,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        try {
            return PdfFontFactory.createFont("/usr/share/fonts/truetype/dejavu/" + cp,
                    PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
        } catch (Exception ignored) {
        }
        return PdfFontFactory.createFont(StandardFonts.HELVETICA);
    }

    private String fmtNum(BigDecimal val) {
        return val == null ? "—" : String.format("%.2f", val);
    }

    private String fmtDate(Object val) {
        return switch (val) {
            case null -> "—";
            case LocalDateTime ldt -> ldt.format(DATE_FMT);
            case LocalDate ld -> ld.format(DATE_ONLY_FMT);
            default -> val.toString();
        };
    }

    private record FooterHandler(PdfFont font) implements IEventHandler {
        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent de = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = de.getDocument();
            PdfPage page = de.getPage();
            Rectangle ps = page.getPageSize();
            PdfCanvas pc = new PdfCanvas(page);
            pc.setStrokeColor(new DeviceRgb(200, 200, 200)).setLineWidth(0.5f)
                    .moveTo(30, 28).lineTo(ps.getWidth() - 30, 28).stroke();
            String txt = "Сторінка " + pdfDoc.getPageNumber(page) +
                         "   |   Система управління доставкою — документ згенеровано автоматично";
            try (Canvas c = new Canvas(pc, ps)) {
                c.setFont(font).setFontSize(7)
                        .setFontColor(new DeviceRgb(130, 130, 130))
                        .showTextAligned(new Paragraph(txt),
                                ps.getWidth() / 2f, 17f,
                                pdfDoc.getPageNumber(page),
                                TextAlignment.CENTER, VerticalAlignment.BOTTOM, 0);
            }
            pc.release();
        }
    }
}