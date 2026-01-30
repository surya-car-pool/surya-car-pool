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
import com.lowagie.text.pdf.BaseFont;
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
			PdfWriter.getInstance(document, out);
			document.open();

			// =========================
			// FONTS (OpenPDF-correct)
			// =========================
			Font titleFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 18, Font.BOLD);

			Font sectionFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 11, Font.BOLD);

			Font normalFont = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, true, 10, Font.NORMAL);

			// =========================
			// LOGO (classpath-safe)
			// =========================
			InputStream logoStream = getClass().getResourceAsStream("/static/images/logo.png");

			if (logoStream != null) {
				Image logo = Image.getInstance(logoStream.readAllBytes());
				logo.scaleToFit(70, 70);
				logo.setAlignment(Element.ALIGN_LEFT);
				document.add(logo);
			}

			// =========================
			// COMPANY HEADER (LOGO COLORS)
			// =========================

			// Logo-matched colors
			Color brandBlue = new Color(30, 58, 138); // #1E3A8A
			Color softGray = new Color(71, 85, 105); // #475569
			Color dividerGray = new Color(226, 232, 240);

			// Fonts with explicit colors
			Font brandFont = new Font(Font.HELVETICA, 20, Font.BOLD, brandBlue);
			Font taglineFont = new Font(Font.HELVETICA, 10, Font.NORMAL, softGray);
			Font contactFont = new Font(Font.HELVETICA, 9, Font.NORMAL, softGray);

			// Company Name
			Paragraph company = new Paragraph("SURYA CAR POOL", brandFont);
			company.setAlignment(Element.ALIGN_RIGHT);
			company.setSpacingAfter(4f);
			document.add(company);

			// Tagline
			Paragraph tagline = new Paragraph("Self Drive Cars Provider", taglineFont);
			tagline.setAlignment(Element.ALIGN_RIGHT);
			tagline.setSpacingAfter(2f);
			document.add(tagline);

			// Contact details
			Paragraph companyInfo = new Paragraph("Hyderabad, India\n+91-9949425597\nsupport@suryacarpool.com",
					contactFont);
			companyInfo.setAlignment(Element.ALIGN_RIGHT);
			companyInfo.setSpacingAfter(10f);
			document.add(companyInfo);

			// Divider
			LineSeparator divider = new LineSeparator();
			divider.setLineColor(dividerGray);
			divider.setLineWidth(1f);
			document.add(divider);

			document.add(Chunk.NEWLINE);

			// =========================
			// INVOICE TITLE
			// =========================
			Paragraph invoiceTitle = new Paragraph("INVOICE\n\n");
			invoiceTitle.setFont(sectionFont);
			invoiceTitle.setAlignment(Element.ALIGN_CENTER);
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
			// PAYMENT SUMMARY
			// =========================
			document.add(new Paragraph("PAYMENT SUMMARY", sectionFont));

			BigDecimal base = booking.getAmount() != null ? booking.getAmount() : BigDecimal.ZERO;

			BigDecimal gst = base.multiply(BigDecimal.valueOf(0.05));
			BigDecimal total = base.add(gst);

			document.add(new Paragraph("Base Amount : ₹ " + base, normalFont));
			document.add(new Paragraph("GST (5%) : ₹ " + gst, normalFont));
			document.add(new Paragraph("----------------------------------", normalFont));
			document.add(new Paragraph("Total Paid : ₹ " + total, sectionFont));
			document.add(new Paragraph("Payment Status : " + (booking.isPaymentConfirmed() ? "PAID" : "PENDING"),
					normalFont));

			document.add(Chunk.NEWLINE);

			// =========================
			// FOOTER
			// =========================
			Paragraph footer = new Paragraph("Thank you for choosing Surya Car Pool!\nDrive Safe 🚗");
			footer.setFont(normalFont);
			footer.setAlignment(Element.ALIGN_CENTER);
			document.add(footer);

			document.close();

		} catch (Exception e) {
			throw new RuntimeException("Failed to generate invoice PDF", e);
		}

		return out.toByteArray();
	}
}
