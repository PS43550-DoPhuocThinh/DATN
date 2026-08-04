package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.example.demoCafePAL.entity.NguoiDung;
import java.util.Optional;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);
}