package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demoCafePAL.entity.DatMon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DatMonRepository extends JpaRepository<DatMon, Integer> {
    Optional<DatMon> findFirstByMaSoBanAndTrangThai(Integer maSoBan, String trangThai);
    List<DatMon> findByTrangThai(String trangThai);
    
    // Lấy toàn bộ đơn đã thanh toán hoặc đã hủy, sắp xếp mới nhất lên đầu
    List<DatMon> findByTrangThaiNotOrderByDuocTaoTaiDesc(String trangThai);

    // Lọc lịch sử theo mã bàn hoặc phương thức thanh toán
    @Query("SELECT d FROM DatMon d WHERE d.trangThai <> 'DANG_PHUC_VU' " +
           "AND (:table IS NULL OR d.maSoBan = :table) " +
           "AND (:payment IS NULL OR d.phuongThucThanhToan = :payment) " +
           "ORDER BY d.duocTaoTai DESC")
    List<DatMon> filterOrderHistory(@Param("table") Integer table, @Param("payment") String payment);
}