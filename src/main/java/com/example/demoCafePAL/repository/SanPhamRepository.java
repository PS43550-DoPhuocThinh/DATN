package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demoCafePAL.entity.SanPham;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    List<SanPham> findByLoai(String loai);
    List<SanPham> findByTenSanPhamContainingIgnoreCase(String keyword);
}