package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demoCafePAL.entity.ChiTietDonHang;
import com.example.demoCafePAL.entity.DatMon;
import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.entity.SanPham;
import com.example.demoCafePAL.repository.ChiTietDonHangRepository;
import com.example.demoCafePAL.repository.DatMonRepository;
import com.example.demoCafePAL.repository.NguoiDungRepository;
import com.example.demoCafePAL.repository.SanPhamRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private DatMonRepository datMonRepo;

    @Autowired
    private ChiTietDonHangRepository chiTietRepo;

    @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private NguoiDungRepository nguoiDungRepo;

    // Lấy đơn hàng hiện tại của bàn (hoặc tạo mới)
    public DatMon getOrCreateActiveOrder(Integer tableNumber, String username) {
        return datMonRepo.findFirstByMaSoBanAndTrangThai(tableNumber, "DANG_PHUC_VU")
                .orElseGet(() -> {
                    DatMon newOrder = new DatMon();
                    newOrder.setMaSoBan(tableNumber);
                    newOrder.setTrangThai("DANG_PHUC_VU");
                    newOrder.setDuocTaoTai(LocalDateTime.now());
                    if (username != null) {
                        NguoiDung user = nguoiDungRepo.findByTenDangNhap(username).orElse(null);
                        newOrder.setDuocTaoBoi(user);
                    }
                    return datMonRepo.save(newOrder);
                });
    }

    // Chỉ tính bàn là "Có khách" khi đơn hàng CÓ ÍT NHẤT 1 MÓN
    public Map<Integer, Boolean> getTableStatusMap(int totalTables) {
        Map<Integer, Boolean> statusMap = new HashMap<>();
        for (int i = 1; i <= totalTables; i++) {
            statusMap.put(i, false); // Mặc định là Trống
        }
        
        List<DatMon> activeOrders = datMonRepo.findByTrangThai("DANG_PHUC_VU");
        for (DatMon order : activeOrders) {
            if (order.getMaSoBan() != null && order.getDanhSachChiTiet() != null && !order.getDanhSachChiTiet().isEmpty()) {
                statusMap.put(order.getMaSoBan(), true); // Chỉ có khách khi đã gọi món
            }
        }
        return statusMap;
    }

    // Thêm món vào bàn
    @Transactional
    public void addItemToTable(Integer tableNumber, Integer maSanPham, String username) {
        DatMon order = getOrCreateActiveOrder(tableNumber, username);
        SanPham sp = sanPhamRepo.findById(maSanPham).orElseThrow();

        ChiTietDonHang existItem = order.getDanhSachChiTiet().stream()
                .filter(ct -> ct.getSanPham().getMaSanPham().equals(maSanPham))
                .findFirst().orElse(null);

        if (existItem != null) {
            existItem.setSoLuong(existItem.getSoLuong() + 1);
            existItem.setTongCong(existItem.getDonGia().multiply(BigDecimal.valueOf(existItem.getSoLuong())));
        } else {
            ChiTietDonHang newItem = new ChiTietDonHang();
            newItem.setDatMon(order);
            newItem.setSanPham(sp);
            newItem.setSoLuong(1);
            newItem.setDonGia(sp.getGia() != null ? sp.getGia() : BigDecimal.ZERO);
            newItem.setTongCong(newItem.getDonGia());
            order.getDanhSachChiTiet().add(newItem);
        }

        recalculateTotal(order);
        datMonRepo.save(order);
    }
    public DatMon getOrderById(Integer id) {
        return datMonRepo.findById(id).orElse(null);
    }
    // Tăng/Giảm số lượng món
    @Transactional
    public void updateQuantity(Integer maChiTiet, int delta) {
        ChiTietDonHang item = chiTietRepo.findById(maChiTiet).orElse(null);
        if (item != null) {
            int newQuantity = item.getSoLuong() + delta;
            DatMon order = item.getDatMon();
            if (newQuantity <= 0) {
                order.getDanhSachChiTiet().remove(item);
                chiTietRepo.delete(item);
            } else {
                item.setSoLuong(newQuantity);
                item.setTongCong(item.getDonGia().multiply(BigDecimal.valueOf(newQuantity)));
                chiTietRepo.save(item);
            }
            recalculateTotal(order);
            datMonRepo.save(order);
        }
    }

    // Xóa một món ra khỏi đơn
    @Transactional
    public void removeItem(Integer maChiTiet) {
        ChiTietDonHang item = chiTietRepo.findById(maChiTiet).orElse(null);
        if (item != null) {
            DatMon order = item.getDatMon();
            order.getDanhSachChiTiet().remove(item);
            chiTietRepo.delete(item);
            recalculateTotal(order);
            datMonRepo.save(order);
        }
    }

    // Hủy đơn hàng và trả bàn về trạng thái trống
    @Transactional
    public void cancelOrClearOrder(Integer maDonHang) {
        DatMon order = datMonRepo.findById(maDonHang).orElse(null);
        if (order != null) {
            order.setTrangThai("DA_HUY");
            order.getDanhSachChiTiet().clear();
            datMonRepo.save(order);
        }
    }

    // Thanh toán đơn hàng và giải phóng bàn
    @Transactional
    public void checkoutOrder(Integer maDonHang, String phuongThuc, BigDecimal chietKhau, BigDecimal phuPhi) {
        DatMon order = datMonRepo.findById(maDonHang).orElseThrow();
        order.setSoTienChietKhau(chietKhau != null ? chietKhau : BigDecimal.ZERO);
        order.setSoTienPhuPhi(phuPhi != null ? phuPhi : BigDecimal.ZERO);
        recalculateTotal(order);

        order.setPhuongThucThanhToan(phuongThuc);
        order.setSoTienDaTra(order.getTongTien());
        if ("TIEN_MAT".equalsIgnoreCase(phuongThuc)) {
            order.setSoTienMat(order.getTongTien());
            order.setSoTienChuyen(BigDecimal.ZERO);
        } else {
            order.setSoTienChuyen(order.getTongTien());
            order.setSoTienMat(BigDecimal.ZERO);
        }

        order.setTrangThai("DA_THANH_TOAN");
        datMonRepo.save(order);
    }

    private void recalculateTotal(DatMon order) {
        BigDecimal sum = BigDecimal.ZERO;
        for (ChiTietDonHang ct : order.getDanhSachChiTiet()) {
            if (ct.getTongCong() != null) {
                sum = sum.add(ct.getTongCong());
            }
        }
        BigDecimal chietKhau = order.getSoTienChietKhau() != null ? order.getSoTienChietKhau() : BigDecimal.ZERO;
        BigDecimal phuPhi = order.getSoTienPhuPhi() != null ? order.getSoTienPhuPhi() : BigDecimal.ZERO;
        order.setTongTien(sum.subtract(chietKhau).add(phuPhi));
    }
}