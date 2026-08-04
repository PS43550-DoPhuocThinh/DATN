package com.example.demoCafePAL.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maNguoiDung;

    @Column(unique = true, nullable = false)
    private String tenDangNhap;

    @Column(nullable = false)
    private String matKhau;

    @Column(nullable = false)
    private String hoTen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MaVaiTro") 
    private VaiTro vaiTro;

    // Getters and Setters
    public Integer getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(Integer maNguoiDung) { this.maNguoiDung = maNguoiDung; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public VaiTro getVaiTro() { return vaiTro; }
    public void setVaiTro(VaiTro vaiTro) { this.vaiTro = vaiTro; }
}