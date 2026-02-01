package com.surya.carpool.booking.service;

import com.surya.carpool.booking.model.Booking;

public interface InvoiceService {

    /**
     * Generates a branded PDF invoice for a booking
     * @param booking Booking entity
     * @return PDF as byte array
     */
    byte[] generateInvoicePdf(Booking booking);
}
