package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demoCafePAL.entity.KhoHang;
import com.example.demoCafePAL.repository.KhoHangRepository;
import java.util.List;

@Service
public class KhoHangService {

    @Autowired
    private KhoHangRepository khoHangRepo;

    public List<KhoHang> getAll() {
        return khoHangRepo.findAll();
    }

    public KhoHang getById(Integer id) {
        return khoHangRepo.findById(id).orElse(null);
    }

    public KhoHang save(KhoHang khoHang) {
        return khoHangRepo.save(khoHang);
    }

    public void delete(Integer id) {
        khoHangRepo.deleteById(id);
    }

    public List<KhoHang> search(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return khoHangRepo.findBySanPham_TenSanPhamContainingIgnoreCase(keyword.trim());
        }
        return khoHangRepo.findAll();
    }
}