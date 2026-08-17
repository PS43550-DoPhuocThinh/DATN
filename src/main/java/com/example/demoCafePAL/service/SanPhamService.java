package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demoCafePAL.entity.SanPham;
import com.example.demoCafePAL.repository.SanPhamRepository;
import java.util.List;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepo;

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

    public List<SanPham> searchByName(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return sanPhamRepo.findByTenSanPhamContainingIgnoreCase(keyword.trim());
        }
        return sanPhamRepo.findAll();
    }
}