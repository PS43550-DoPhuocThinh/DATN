package com.example.demoCafePAL.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demoCafePAL.entity.NguoiDung;
import com.example.demoCafePAL.repository.NguoiDungRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findByTenDangNhap(username)
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại!"));

        String roleName = (nguoiDung.getVaiTro() != null) ? nguoiDung.getVaiTro().getTenVaiTro() : "GUEST";

        // Gán trực tiếp quyền (Authority) không bị tự ép thêm tiền tố ROLE_
        return new User(
                nguoiDung.getTenDangNhap(),
                nguoiDung.getMatKhau(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}