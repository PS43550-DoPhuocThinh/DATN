package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demoCafePAL.entity.KhoHang;
import java.util.List;

@Repository
public interface KhoHangRepository extends JpaRepository<KhoHang, Integer> {
    List<KhoHang> findBySanPham_TenSanPhamContainingIgnoreCase(String keyword);
}