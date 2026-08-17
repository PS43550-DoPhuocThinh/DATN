package com.example.demoCafePAL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.entity.VaiTro;
import com.example.demoCafePAL.repository.NguoiDungRepository;
import com.example.demoCafePAL.repository.VaiTroRepository;

@Controller
public class AuthController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Xử lý trang chủ tại đây
    @GetMapping({"/", "/home", "/index"})
    public String homePage(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated() 
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            model.addAttribute("currentUser", authentication.getName());
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("isAdmin", isAdmin);
        } else {
            model.addAttribute("currentUser", null);
            model.addAttribute("isAdmin", false);
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register"; 
    }

    @PostMapping("/register")
    public String processRegister(NguoiDung nguoiDung) {
        nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        
        VaiTro roleGuest = vaiTroRepository.findByTenVaiTro("GUEST")
                .orElseGet(() -> {
                    VaiTro newRole = new VaiTro();
                    newRole.setTenVaiTro("GUEST");
                    return vaiTroRepository.save(newRole);
                });
        nguoiDung.setVaiTro(roleGuest);
        
        nguoiDungRepository.save(nguoiDung);
        
        return "redirect:/login?registered"; 
    }
}