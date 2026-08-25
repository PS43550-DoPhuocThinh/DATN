package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demoCafePAL.entity.SanPham;
import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    List<SanPham> findByTenSanPhamContainingIgnoreCase(String keyword);
    List<SanPham> findByLoai(String loai);
    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndLoai(String keyword, String loai);

    // Lấy danh sách tất cả các loại danh mục duy nhất trong database
    @Query("SELECT DISTINCT s.loai FROM SanPham s WHERE s.loai IS NOT NULL AND TRIM(s.loai) <> ''")
    List<String> findDistinctCategories();
}