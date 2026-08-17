package com.example.demoCafePAL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demoCafePAL.entity.SanPham;
import com.example.demoCafePAL.service.SanPhamService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("products", sanPhamService.searchByName(keyword));
        model.addAttribute("keyword", keyword);
        return "product-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new SanPham());
        return "product-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        SanPham product = sanPhamService.getSanPhamById(id);
        if (product != null) {
            model.addAttribute("product", product);
            return "product-form";
        }
        return "redirect:/products";
    }

    // Xử lý lưu món kèm file ảnh upload
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") SanPham product,
                              @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        // Kiểm tra nếu người dùng có chọn file ảnh mới
        if (!imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            Path uploadPath = Paths.get("./uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = imageFile.getInputStream()) {
                Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                product.setHinhAnh("/uploads/" + uniqueFileName);
            }
        } else if (product.getMaSanPham() != null) {
            // Khi sửa mà không chọn ảnh mới, giữ lại ảnh cũ
            SanPham oldProduct = sanPhamService.getSanPhamById(product.getMaSanPham());
            if (oldProduct != null) {
                product.setHinhAnh(oldProduct.getHinhAnh());
            }
        }

        sanPhamService.saveSanPham(product);
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id) {
        sanPhamService.deleteSanPham(id);
        return "redirect:/products";
    }
}