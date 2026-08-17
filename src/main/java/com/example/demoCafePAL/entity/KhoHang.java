package com.example.demoCafePAL.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Entity
@Table(name = "KhoHang")
public class KhoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaKho")
    private Integer maKho;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaSanPham", nullable = false)
    private SanPham sanPham;

    @Column(name = "NhaCungCap", length = 100)
    private String nhaCungCap;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "NgayNhapKho")
    private LocalDate ngayNhapKho;

    @Column(name = "SoLuongCon")
    private Integer soLuongCon;

    public KhoHang() {
        this.ngayNhapKho = LocalDate.now();
    }

    public KhoHang(SanPham sanPham, String nhaCungCap, LocalDate ngayNhapKho, Integer soLuongCon) {
        this.sanPham = sanPham;
        this.nhaCungCap = nhaCungCap;
        this.ngayNhapKho = ngayNhapKho;
        this.soLuongCon = soLuongCon;
    }

    // Getters and Setters
    public Integer getMaKho() { return maKho; }
    public void setMaKho(Integer maKho) { this.maKho = maKho; }

    public SanPham getSanPham() { return sanPham; }
    public void setSanPham(SanPham sanPham) { this.sanPham = sanPham; }

    public String getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(String nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public LocalDate getNgayNhapKho() { return ngayNhapKho; }
    public void setNgayNhapKho(LocalDate ngayNhapKho) { this.ngayNhapKho = ngayNhapKho; }

    public Integer getSoLuongCon() { return soLuongCon; }
    public void setSoLuongCon(Integer soLuongCon) { this.soLuongCon = soLuongCon; }
}