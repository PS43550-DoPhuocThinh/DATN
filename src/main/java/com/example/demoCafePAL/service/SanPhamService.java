package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demoCafePAL.entity.SanPham;
import com.example.demoCafePAL.repository.SanPhamRepository;

import java.util.*;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepo;

    // Danh mục mặc định gốc của PAL COFFEE
    private static final List<String> DEFAULT_CATEGORIES = Arrays.asList(
        "Cà phê & Cacao",
        "Trà trái cây",
        "Nước ép tươi",
        "Sinh tố",
        "Nước giải khát & Khác"
    );

    // Tự động gộp danh mục mặc định + các loại mới người dùng tự tạo
    public List<String> getAllCategories() {
        Set<String> categories = new LinkedHashSet<>(DEFAULT_CATEGORIES);
        List<String> dbCategories = sanPhamRepo.findDistinctCategories();
        if (dbCategories != null) {
            categories.addAll(dbCategories);
        }
        return new ArrayList<>(categories);
    }

    public List<SanPham> getAllSanPham() {
        return sanPhamRepo.findAll();
    }

    public SanPham getSanPhamById(Integer id) {
        return sanPhamRepo.findById(id).orElse(null);
    }

    public SanPham saveSanPham(SanPham sanPham) {
        return sanPhamRepo.save(sanPham);
    }

    public void deleteSanPham(Integer id) {
        sanPhamRepo.deleteById(id);
    }

    public List<SanPham> searchAndFilter(String keyword, String category) {
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasCategory = (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category));

        if (hasKeyword && hasCategory) {
            return sanPhamRepo.findByTenSanPhamContainingIgnoreCaseAndLoai(keyword.trim(), category.trim());
        } else if (hasKeyword) {
            return sanPhamRepo.findByTenSanPhamContainingIgnoreCase(keyword.trim());
        } else if (hasCategory) {
            return sanPhamRepo.findByLoai(category.trim());
        }
        return sanPhamRepo.findAll();
    }
}