package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demoCafePAL.entity.DatMon;
import java.util.List;
import java.util.Optional;

@Repository
public interface DatMonRepository extends JpaRepository<DatMon, Integer> {
    Optional<DatMon> findFirstByMaSoBanAndTrangThai(Integer maSoBan, String trangThai);
    List<DatMon> findByTrangThai(String trangThai);
}