package com.surya.carpool.aboutus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aboutus")
public class AboutUsController {

	@GetMapping("/ui")
	public String aboutUsPage() {
		return "aboutus"; // aboutus.html
	}
}
