package com.surya.carpool.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.surya.carpool.owner.service.OwnerTermsService;
import com.surya.carpool.user.model.Role;
import com.surya.carpool.user.model.User;
import com.surya.carpool.user.repository.UserRepository;

@Controller
public class CarAdminUIController {

    private final OwnerTermsService ownerTermsService;
    private final UserRepository userRepository;

    public CarAdminUIController(OwnerTermsService ownerTermsService, UserRepository userRepository) {
        this.ownerTermsService = ownerTermsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/attachcar/ui")
    public String attachCarPage(@AuthenticationPrincipal User user, Model model) {
        User currentAccount = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("Signed-in account no longer exists"));
        if (currentAccount.getRole() == Role.USER) {
            return "redirect:/owner/terms";
        }
        if (currentAccount.getRole() == Role.OWNER) {
            if (!ownerTermsService.hasAcceptedCurrentTerms(currentAccount)) {
                return "redirect:/owner/terms";
            }
            model.addAttribute("ownerName", currentAccount.getName());
            return "owner-vehicle";
        }
        if (currentAccount.getRole() != Role.ADMIN) {
            return "redirect:/home";
        }
        return "attachcar"; 
    }
}
