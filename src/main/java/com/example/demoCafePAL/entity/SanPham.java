package com.example.demoCafePAL.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SanPham")
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSanPham")
    private Integer maSanPham;

    @Column(name = "TenSanPham", length = 100)
    private String tenSanPham;

    @Column(name = "Loai", length = 50)
    private String loai;

    @Column(name = "KichSize", length = 30)
    private String kichCo;

    @Column(name = "Gia", precision = 18, scale = 2)
    private BigDecimal gia;

    @Column(name = "HinhAnh", length = 255)
    private String hinhAnh;

    public SanPham() {}

    public SanPham(String tenSanPham, String loai, String kichCo, BigDecimal gia, String hinhAnh) {
        this.tenSanPham = tenSanPham;
        this.loai = loai;
        this.kichCo = kichCo;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
    }

    // Getters and Setters
    public Integer getMaSanPham() { return maSanPham; }
    public void setMaSanPham(Integer maSanPham) { this.maSanPham = maSanPham; }
    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }
    public String getKichCo() { return kichCo; }
    public void setKichCo(String kichCo) { this.kichCo = kichCo; }
    public BigDecimal getGia() { return gia; }
    public void setGia(BigDecimal gia) { this.gia = gia; }
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
}