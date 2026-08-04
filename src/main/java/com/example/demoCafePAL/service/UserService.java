package com.example.demoCafePAL.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.repository.NguoiDungRepository;

@Service
public class UserService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Read: Lấy danh sách tất cả người dùng
    public List<NguoiDung> getAllUsers() {
        return nguoiDungRepository.findAll();
    }

    // 2. Read: Tìm 1 người dùng theo ID
    public NguoiDung getUserById(Integer id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    // 3. Create & Update: Lưu hoặc Cập nhật người dùng
    public void saveUser(NguoiDung nguoiDung) {
        // Nếu là thêm mới hoặc đổi mật khẩu thì mã hóa lại
        if (nguoiDung.getMaNguoiDung() == null || !nguoiDung.getMatKhau().startsWith("$2a$")) {
            nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDung.getMatKhau()));
        }
        nguoiDungRepository.save(nguoiDung);
    }

    // 4. Delete: Xóa người dùng
    public void deleteUser(Integer id) {
        nguoiDungRepository.deleteById(id);
    }
}