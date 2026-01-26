package com.surya.carpool.contact;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactUsController {

	@GetMapping("/contactus/ui")
	public String contactUsPage() {
		return "contactus"; // contactus.html
	}
}
