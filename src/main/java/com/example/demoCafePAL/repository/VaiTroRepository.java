package com.example.demoCafePAL.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import  com.example.demoCafePAL.entity.VaiTro;
import java.util.Optional;

public interface VaiTroRepository extends JpaRepository<VaiTro, Integer> {
    Optional<VaiTro> findByTenVaiTro(String tenVaiTro);
}