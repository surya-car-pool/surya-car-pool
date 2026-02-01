package com.surya.carpool.booking.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.surya.carpool.booking.model.Booking;

@Service
public class InvoiceServiceImpl implements InvoiceService {

	private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

	@Override
	public byte[] generateInvoicePdf(Booking booking) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document(PageSize.A4, 36, 36, 36, 36);

		try {
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();

			// ==================================================
			// WATERMARK (CENTER, BACKGROUND, DIAGONAL)
			// ==================================================
			Font watermarkFont = new Font(Font.HELVETICA, 52, Font.BOLD, new Color(226, 232, 240));

			Phrase watermarkPhrase = new Phrase("SURYA CAR POOL", watermarkFont);

			PdfContentByte canvas = writer.getDirectContentUnder();

			ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, watermarkPhrase, PageSize.A4.getWidth() / 2,
					PageSize.A4.getHeight() / 2, 45);

			// =========================
			// FONTS
			// =========================
			Font titleFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 18, Font.BOLD);

			Font sectionFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 11, Font.BOLD);

			Font normalFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 10, Font.NORMAL);

			// =========================
			// LOGO
			// =========================
			InputStream logoStream = getClass().getResourceAsStream("/static/images/logo.png");

			if (logoStream != null) {
				Image logo = Image.getInstance(logoStream.readAllBytes());
				logo.scaleToFit(70, 70);
				logo.setAlignment(Element.ALIGN_LEFT);
				document.add(logo);
			}

			// =========================
			// COMPANY HEADER
			// =========================
			Color brandBlue = new Color(30, 58, 138);
			Color softGray = new Color(71, 85, 105);
			Color dividerGray = new Color(226, 232, 240);

			Font brandFont = new Font(Font.HELVETICA, 20, Font.BOLD, brandBlue);
			Font taglineFont = new Font(Font.HELVETICA, 10, Font.NORMAL, softGray);
			Font contactFont = new Font(Font.HELVETICA, 9, Font.NORMAL, softGray);

			Paragraph company = new Paragraph("SURYA CAR POOL", brandFont);
			company.setAlignment(Element.ALIGN_RIGHT);
			document.add(company);

			Paragraph tagline = new Paragraph("Self Drive Cars Provider", taglineFont);
			tagline.setAlignment(Element.ALIGN_RIGHT);
			document.add(tagline);

			Paragraph companyInfo = new Paragraph("Hyderabad, India\n+91-9949425597\nsupport@suryacarpool.com",
					contactFont);
			companyInfo.setAlignment(Element.ALIGN_RIGHT);
			companyInfo.setSpacingAfter(10f);
			document.add(companyInfo);

			LineSeparator divider = new LineSeparator();
			divider.setLineColor(dividerGray);
			document.add(divider);
			document.add(Chunk.NEWLINE);

			Paragraph gstInfo = new Paragraph("GSTIN : 36ABCDE1234F1Z5\nTAX INVOICE",
					new Font(Font.HELVETICA, 9, Font.BOLD));
			gstInfo.setAlignment(Element.ALIGN_RIGHT);
			gstInfo.setSpacingAfter(12f);
			document.add(gstInfo);

			// =========================
			// INVOICE TITLE
			// =========================
			Paragraph invoiceTitle = new Paragraph("INVOICE", sectionFont);
			invoiceTitle.setAlignment(Element.ALIGN_CENTER);
			invoiceTitle.setSpacingAfter(15f);
			document.add(invoiceTitle);

			// =========================
			// INVOICE META
			// =========================
			document.add(new Paragraph("Invoice No : SCP-INV-" + booking.getId(), normalFont));
			document.add(new Paragraph("Booking ID : " + booking.getId(), normalFont));
			document.add(new Paragraph("Invoice Date : " + booking.getCreatedAt().toLocalDate(), normalFont));
			document.add(Chunk.NEWLINE);

			// =========================
			// CUSTOMER DETAILS
			// =========================
			document.add(new Paragraph("CUSTOMER DETAILS", sectionFont));
			document.add(new Paragraph("Name : " + booking.getCustomerName(), normalFont));
			document.add(new Paragraph("Email : " + booking.getEmail(), normalFont));
			document.add(new Paragraph("Phone : " + booking.getPhone(), normalFont));
			document.add(Chunk.NEWLINE);

			// =========================
			// BOOKING DETAILS
			// =========================
			document.add(new Paragraph("BOOKING DETAILS", sectionFont));
			document.add(new Paragraph("Car : " + booking.getCar().getMake(), normalFont));
			document.add(new Paragraph("Pickup Location : " + booking.getPickupLocation(), normalFont));
			document.add(new Paragraph("Pickup Date : " + booking.getPickupDateTime().format(DATE_FMT), normalFont));
			document.add(new Paragraph("Drop Date : " + booking.getDropDateTime().format(DATE_FMT), normalFont));
			document.add(new Paragraph("Payment Method : " + booking.getPaymentMethod(), normalFont));
			document.add(Chunk.NEWLINE);

			// =========================
			// PAYMENT SUMMARY (TABLE)
			// =========================
			document.add(new Paragraph("PAYMENT SUMMARY", sectionFont));
			document.add(Chunk.NEWLINE);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(60);
			table.setWidths(new float[] { 3f, 2f });

			BigDecimal base = booking.getAmount() != null ? booking.getAmount() : BigDecimal.ZERO;

			BigDecimal gst = base.multiply(BigDecimal.valueOf(0.05));
			BigDecimal total = base.add(gst);

			Font tableLabelFont = new Font(Font.HELVETICA, 10);
			Font tableValueFont = new Font(Font.HELVETICA, 10, Font.BOLD);

			PdfPCell cell;

			cell = new PdfPCell(new Phrase("Base Amount", tableLabelFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("₹ " + base, tableValueFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("GST (5%)", tableLabelFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			cell = new PdfPCell(new Phrase("₹ " + gst, tableValueFont));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			document.add(table);
			document.add(Chunk.NEWLINE);

			// =========================
			// TOTAL & STATUS
			// =========================
			Color successGreen = new Color(22, 163, 74);
			Color warningOrange = new Color(234, 88, 12);

			Font totalFont = new Font(Font.HELVETICA, 14, Font.BOLD, successGreen);
			Font statusFont = new Font(Font.HELVETICA, 10, Font.BOLD,
					booking.isPaymentConfirmed() ? successGreen : warningOrange);

			document.add(new Paragraph("Total Paid : ₹ " + total, totalFont));

			document.add(new Paragraph("Payment Status : " + (booking.isPaymentConfirmed() ? "PAID" : "PENDING"),
					statusFont));

			// =========================
			// FOOTER
			// =========================
			Paragraph footer = new Paragraph("\nThank you for choosing Surya Car Pool!\nDrive Safe 🚗", normalFont);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.add(footer);

			document.close();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate invoice PDF", e);
		}

		return out.toByteArray();
	}
}
