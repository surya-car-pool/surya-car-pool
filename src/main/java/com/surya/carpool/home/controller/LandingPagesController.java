package com.surya.carpool.home.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPagesController {

	@GetMapping({ "/customer-app", "/customer-app/ui" })
	public String customerApp() {
		return "customer-app";
	}

	@GetMapping({ "/owner-app", "/owner-app/ui" })
	public String ownerApp() {
		return "owner-app";
	}

	@GetMapping("/terms")
	public String terms(Model model) {
		model.addAttribute("pageTitle", "Terms & Conditions");
		model.addAttribute("pageIntro",
				"Please review the rental terms shown during the booking process before confirming a car.");
		model.addAttribute("pageSections", List.of(
				"Booking-specific requirements and cancellation information are presented in the existing booking flow.",
				"Vehicle availability, pricing, and rental details should be confirmed before you proceed.",
				"For questions about the terms shown for a booking, contact the SuryaCarPool team using the contact page."));
		return "information";
	}

	@GetMapping("/privacy")
	public String privacy(Model model) {
		model.addAttribute("pageTitle", "Privacy");
		model.addAttribute("pageIntro",
				"SuryaCarPool uses account, booking, and owner-submission information to operate the services available in this application.");
		model.addAttribute("pageSections", List.of(
				"Provide only accurate information needed for your account or a booking.",
				"Owner and booking forms may request identity or vehicle documents for the existing application workflow. Use care with sensitive information.",
				"To ask about information associated with your account, contact SuryaCarPool through the contact page."));
		return "information";
	}

	@GetMapping("/faq")
	public String faq(Model model) {
		model.addAttribute("pageTitle", "Frequently Asked Questions");
		model.addAttribute("pageIntro", "Quick answers about the current SuryaCarPool web experience.");
		model.addAttribute("questions", List.of(
				Map.of("question", "Can I list a commercial fleet vehicle?",
						"answer", "SuryaCarPool is focused on privately owned cars submitted by their registered owners."),
				Map.of("question", "How do I browse cars?",
						"answer", "Sign in and open the available cars page to review cars and their displayed daily rates."),
				Map.of("question", "Where can I see booking requirements?",
						"answer", "The current booking page presents rental requirements before you continue."),
				Map.of("question", "Are native customer and owner apps available?",
						"answer", "The current product is the responsive web application. Native app downloads are not included in this repository."),
				Map.of("question", "How can I get help?",
						"answer", "Use the contact page to call or email SuryaCarPool support.")));
		return "faq";
	}
}
