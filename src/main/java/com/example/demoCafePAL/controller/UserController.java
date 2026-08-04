package com.example.demoCafePAL.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.entity.VaiTro;
import com.example.demoCafePAL.repository.VaiTroRepository;
import com.example.demoCafePAL.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private VaiTroRepository vaiTroRepository;

    // 1. Hiển thị danh sách (Read)
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<NguoiDung> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "user-list";
    }

    // 2. Hiển thị form Thêm mới (Create)
    @GetMapping("/users/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new NguoiDung());
        // Lấy danh sách Vai trò để hiển thị trong <select>
        List<VaiTro> roles = vaiTroRepository.findAll();
        model.addAttribute("roles", roles);
        return "user-form";
    }

    // 3. Xử lý lưu dữ liệu (cho cả Thêm mới và Cập nhật)
    @PostMapping("/users/save")
    public String saveUser(NguoiDung user) {
        userService.saveUser(user);
        return "redirect:/users"; // Lưu xong quay về trang danh sách
    }

    // 4. Hiển thị form Cập nhật (Update)
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        NguoiDung user = userService.getUserById(id);
        if (user != null) {
            model.addAttribute("user", user);
            List<VaiTro> roles = vaiTroRepository.findAll();
            model.addAttribute("roles", roles);
            return "user-form";
        }
        return "redirect:/users";
    }

    // 5. Xử lý Xóa (Delete)
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}