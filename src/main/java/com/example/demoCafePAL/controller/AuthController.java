package com.example.demoCafePAL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
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

    // Để GlobalControllerAdvice tự động xử lý thông tin tài khoản và quyền hạn
    @GetMapping({"/", "/home", "/index"})
    public String homePage() {
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

        return "redirect:/login?registered=true";
    }
}