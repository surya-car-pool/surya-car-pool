package com.surya.carpool.booking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyBookingsUIController {

    @GetMapping("/mybookings/ui")
    public String myBookingsPage() {
        return "mybookings"; // mybookings.html
    }
}
