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
        .authorizeHttpRequests(auth -> auth
        	    // Thêm dòng này để phân quyền:
        	    .requestMatchers("/users/**").hasRole("ADMIN") 
        	    
        	    .requestMatchers("/admin/**").hasRole("ADMIN")
        	    .requestMatchers("/staff/**").hasAnyRole("ADMIN", "STAFF")
        	    .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/error").permitAll()
        	    .anyRequest().authenticated()
        	)
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login?error")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("CafePalSecretKey")
                .tokenValiditySeconds(86400) // Ghi nhớ 1 ngày
                .rememberMeParameter("remember-me")
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}