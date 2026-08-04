package com.example.demoCafePAL.entity;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "VaiTro")
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maVaiTro;
    
    private String tenVaiTro; 

    @OneToMany(mappedBy = "vaiTro")
    private List<NguoiDung> danhSachNguoiDung;

    // Getters and Setters
    public Integer getMaVaiTro() { return maVaiTro; }
    public void setMaVaiTro(Integer maVaiTro) { this.maVaiTro = maVaiTro; }
    public String getTenVaiTro() { return tenVaiTro; }
    public void setTenVaiTro(String tenVaiTro) { this.tenVaiTro = tenVaiTro; }
}