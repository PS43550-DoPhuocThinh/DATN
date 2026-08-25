package com.example.demoCafePAL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demoCafePAL.entity.DatMon;
import com.example.demoCafePAL.service.OrderService;
import com.example.demoCafePAL.service.SanPhamService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/pos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public String showPos(@RequestParam(value = "table", required = false, defaultValue = "1") Integer tableNumber,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "category", required = false, defaultValue = "ALL") String category,
                          Authentication auth, Model model) {
        String username = (auth != null) ? auth.getName() : null;

        model.addAttribute("tableStatusMap", orderService.getTableStatusMap(12));
        model.addAttribute("currentTable", tableNumber);

        DatMon order = orderService.getOrCreateActiveOrder(tableNumber, username);
        model.addAttribute("order", order);

        model.addAttribute("menuItems", sanPhamService.searchAndFilter(keyword, category));
        model.addAttribute("categories", sanPhamService.getAllCategories());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("keyword", keyword);

        return "pos";
    }

    @PostMapping("/add-item")
    public String addItem(@RequestParam("table") Integer tableNumber,
                          @RequestParam("maSanPham") Integer maSanPham,
                          Authentication auth) {
        String username = (auth != null) ? auth.getName() : null;
        orderService.addItemToTable(tableNumber, maSanPham, username);
        return "redirect:/pos?table=" + tableNumber;
    }

    @GetMapping("/update-qty")
    public String updateQty(@RequestParam("table") Integer tableNumber,
                            @RequestParam("maChiTiet") Integer maChiTiet,
                            @RequestParam("delta") int delta) {
        orderService.updateQuantity(maChiTiet, delta);
        return "redirect:/pos?table=" + tableNumber;
    }

    @GetMapping("/remove-item")
    public String removeItem(@RequestParam("table") Integer tableNumber,
                             @RequestParam("maChiTiet") Integer maChiTiet) {
        orderService.removeItem(maChiTiet);
        return "redirect:/pos?table=" + tableNumber;
    }

    @PostMapping("/clear-table")
    public String clearTable(@RequestParam("maDonHang") Integer maDonHang,
                             @RequestParam("table") Integer tableNumber) {
        orderService.cancelOrClearOrder(maDonHang);
        return "redirect:/pos?table=" + tableNumber;
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("maDonHang") Integer maDonHang,
                           @RequestParam("table") Integer tableNumber,
                           @RequestParam("phuongThuc") String phuongThuc,
                           @RequestParam(value = "chietKhau", defaultValue = "0") BigDecimal chietKhau,
                           @RequestParam(value = "phuPhi", defaultValue = "0") BigDecimal phuPhi) {
        orderService.checkoutOrder(maDonHang, phuongThuc, chietKhau, phuPhi);
        return "redirect:/pos/bill/" + maDonHang;
    }

    @GetMapping("/bill/{id}")
    public String viewBill(@PathVariable("id") Integer id, Model model) {
        DatMon order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/pos";
        }
        model.addAttribute("order", order);
        return "bill";
    }
 // Thêm phương thức này vào OrderController

    @GetMapping("/history")
    public String showOrderHistory(@RequestParam(value = "table", required = false) Integer table,
                                   @RequestParam(value = "payment", required = false, defaultValue = "ALL") String payment,
                                   Model model) {
        List<DatMon> historyOrders = orderService.getOrderHistory(table, payment);
        BigDecimal totalRevenue = orderService.calculateTotalRevenue(historyOrders);

        long totalCash = historyOrders.stream()
                .filter(o -> "TIEN_MAT".equalsIgnoreCase(o.getPhuongThucThanhToan()) && "DA_THANH_TOAN".equalsIgnoreCase(o.getTrangThai()))
                .count();

        long totalTransfer = historyOrders.stream()
                .filter(o -> "CHUYEN_KHOAN".equalsIgnoreCase(o.getPhuongThucThanhToan()) && "DA_THANH_TOAN".equalsIgnoreCase(o.getTrangThai()))
                .count();

        model.addAttribute("orders", historyOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalCount", historyOrders.size());
        model.addAttribute("totalCash", totalCash);
        model.addAttribute("totalTransfer", totalTransfer);
        model.addAttribute("selectedTable", table);
        model.addAttribute("selectedPayment", payment);

        return "order-history";
    }
}