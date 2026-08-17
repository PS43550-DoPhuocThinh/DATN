package com.example.demoCafePAL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demoCafePAL.entity.KhoHang;
import com.example.demoCafePAL.service.KhoHangService;
import com.example.demoCafePAL.service.SanPhamService;

@Controller
@RequestMapping("/inventory")
public class KhoHangController {

    @Autowired
    private KhoHangService khoHangService;

    @Autowired
    private SanPhamService sanPhamService;

    // 1. Xem danh sách hàng trong kho
    @GetMapping
    public String listInventory(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("inventories", khoHangService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "inventory-list";
    }

    // 2. Form nhập kho mới
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new KhoHang());
        model.addAttribute("products", sanPhamService.getAllSanPham());
        return "inventory-form";
    }

    // 3. Form chỉnh sửa lô hàng nhập
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        KhoHang item = khoHangService.getById(id);
        if (item != null) {
            model.addAttribute("inventory", item);
            model.addAttribute("products", sanPhamService.getAllSanPham());
            return "inventory-form";
        }
        return "redirect:/inventory";
    }

    // 4. Lưu thông tin nhập kho
    @PostMapping("/save")
    public String saveInventory(@ModelAttribute("inventory") KhoHang khoHang) {
        khoHangService.save(khoHang);
        return "redirect:/inventory";
    }

    // 5. Xóa dữ liệu kho
    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable("id") Integer id) {
        khoHangService.delete(id);
        return "redirect:/inventory";
    }
}