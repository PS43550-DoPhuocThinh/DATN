package com.example.demoCafePAL.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            model.addAttribute("currentUser", auth.getName());
            
            boolean isAdmin = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("ADMIN"::equalsIgnoreCase);
            
            boolean isStaff = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch("STAFF"::equalsIgnoreCase);

            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("isStaff", isStaff);
            model.addAttribute("isStaffOrAdmin", isAdmin || isStaff);
        } else {
            model.addAttribute("currentUser", null);
            model.addAttribute("isAdmin", false);
            model.addAttribute("isStaff", false);
            model.addAttribute("isStaffOrAdmin", false);
        }
    }
}