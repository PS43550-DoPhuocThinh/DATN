package com.example.demoCafePAL.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "DatMon")
public class DatMon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDonHang")
    private Integer maDonHang;

    @Column(name = "MaSoBan")
    private Integer maSoBan;

    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien = BigDecimal.ZERO;

    @Column(name = "SoTienPhuPhi", precision = 18, scale = 2)
    private BigDecimal soTienPhuPhi = BigDecimal.ZERO;

    @Column(name = "SoTienChietKhau", precision = 18, scale = 2)
    private BigDecimal soTienChietKhau = BigDecimal.ZERO;

    @Column(name = "SoTienDaTra", precision = 18, scale = 2)
    private BigDecimal soTienDaTra = BigDecimal.ZERO;

    @Column(name = "SoTienChuyen", precision = 18, scale = 2)
    private BigDecimal soTienChuyen = BigDecimal.ZERO;

    @Column(name = "SoTienMat", precision = 18, scale = 2)
    private BigDecimal soTienMat = BigDecimal.ZERO;

    @Column(name = "PhuongThucThanhToan", length = 50)
    private String phuongThucThanhToan;

    @Column(name = "TrangThai", length = 30)
    private String trangThai; // "DANG_PHUC_VU", "DA_THANH_TOAN", "DA_HUY"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DuocTaoBoi")
    private NguoiDung duocTaoBoi;

    @Column(name = "DuocTaoTai")
    private LocalDateTime duocTaoTai;

    @OneToMany(mappedBy = "datMon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonHang> danhSachChiTiet = new ArrayList<>();

    public DatMon() {}

    // Getters and Setters
    public Integer getMaDonHang() { return maDonHang; }
    public void setMaDonHang(Integer maDonHang) { this.maDonHang = maDonHang; }
    public Integer getMaSoBan() { return maSoBan; }
    public void setMaSoBan(Integer maSoBan) { this.maSoBan = maSoBan; }
    public BigDecimal getTongTien() { return tongTien; }
    public void setTongTien(BigDecimal tongTien) { this.tongTien = tongTien; }
    public BigDecimal getSoTienPhuPhi() { return soTienPhuPhi; }
    public void setSoTienPhuPhi(BigDecimal soTienPhuPhi) { this.soTienPhuPhi = soTienPhuPhi; }
    public BigDecimal getSoTienChietKhau() { return soTienChietKhau; }
    public void setSoTienChietKhau(BigDecimal soTienChietKhau) { this.soTienChietKhau = soTienChietKhau; }
    public BigDecimal getSoTienDaTra() { return soTienDaTra; }
    public void setSoTienDaTra(BigDecimal soTienDaTra) { this.soTienDaTra = soTienDaTra; }
    public BigDecimal getSoTienChuyen() { return soTienChuyen; }
    public void setSoTienChuyen(BigDecimal soTienChuyen) { this.soTienChuyen = soTienChuyen; }
    public BigDecimal getSoTienMat() { return soTienMat; }
    public void setSoTienMat(BigDecimal soTienMat) { this.soTienMat = soTienMat; }
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public NguoiDung getDuocTaoBoi() { return duocTaoBoi; }
    public void setDuocTaoBoi(NguoiDung duocTaoBoi) { this.duocTaoBoi = duocTaoBoi; }
    public LocalDateTime getDuocTaoTai() { return duocTaoTai; }
    public void setDuocTaoTai(LocalDateTime duocTaoTai) { this.duocTaoTai = duocTaoTai; }
    public List<ChiTietDonHang> getDanhSachChiTiet() { return danhSachChiTiet; }
    public void setDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet) { this.danhSachChiTiet = danhSachChiTiet; }
}