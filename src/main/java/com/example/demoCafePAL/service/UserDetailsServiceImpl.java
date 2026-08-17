package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.repository.NguoiDungRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại!"));

        String roleName = (nguoiDung.getVaiTro() != null) ? nguoiDung.getVaiTro().getTenVaiTro() : "GUEST";
        
        // Loại bỏ tiền tố ROLE_ nếu có sẵn trong DB để tránh bị nhân đôi ROLE_ROLE_
        if (roleName.startsWith("ROLE_")) {
            roleName = roleName.substring(5);
        }

        return User.builder()
                .username(nguoiDung.getTenDangNhap())
                .password(nguoiDung.getMatKhau())
                .roles(roleName) 
                .build();
    }
}