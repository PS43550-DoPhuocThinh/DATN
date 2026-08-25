package com.example.demoCafePAL.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Công khai: Xem thực đơn, Trang chủ, Đăng nhập, Đăng ký, Tệp tĩnh
                .requestMatchers("/", "/home", "/index", "/login", "/register", "/products", "/uploads/**", "/css/**", "/js/**", "/images/**").permitAll()
                
                // Màn hình Bán hàng (POS): Cả Nhân viên (STAFF) và Quản lý (ADMIN) đều dùng được
                .requestMatchers("/pos/**").hasAnyAuthority("ADMIN", "STAFF")
                
                // Quản lý Kho hàng, Thêm/Sửa/Xóa Món, Quản lý Nhân sự: CHỈ DÀNH CHO ADMIN
                .requestMatchers("/inventory/**", "/users/**", "/products/add", "/products/edit/**", "/products/save", "/products/delete/**").hasAuthority("ADMIN")
                
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("tenDangNhap")
                .passwordParameter("matKhau")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}