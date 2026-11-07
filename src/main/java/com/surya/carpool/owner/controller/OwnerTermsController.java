package com.surya.carpool.owner.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.surya.carpool.owner.service.OwnerTermsService;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class OwnerTermsController {

	private final OwnerTermsService ownerTermsService;

	public OwnerTermsController(OwnerTermsService ownerTermsService) {
		this.ownerTermsService = ownerTermsService;
	}

	@GetMapping("/owner/terms")
	public String terms(@AuthenticationPrincipal User owner, Model model) {
		if (owner.getRole() != Role.USER && owner.getRole() != Role.OWNER) {
			return "redirect:/home";
		}
		if (ownerTermsService.hasAcceptedCurrentTerms(owner)) {
			return "redirect:/attachcar/ui";
		}
		model.addAttribute("termsVersion", ownerTermsService.getCurrentTermsVersion());
		return "owner-terms";
	}

	@PostMapping("/owner/terms/accept")
	public String acceptTerms(@AuthenticationPrincipal User owner,
			@RequestParam(name = "accepted", defaultValue = "false") boolean accepted,
			HttpServletRequest request) {
		if (!accepted || (owner.getRole() != Role.USER && owner.getRole() != Role.OWNER)) {
			return "redirect:/owner/terms?error";
		}
		ownerTermsService.acceptCurrentTerms(owner, request.getRemoteAddr());
		return "redirect:/attachcar/ui";
	}
}
