package com.surya.carpool.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CarAdminUIController {

    @GetMapping("/attachcar/ui")
    public String attachCarPage() {
        return "attachcar";
    }
}
