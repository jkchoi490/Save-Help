package com.save_help.Save_Help.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화 (Postman 테스트용)
                .csrf(csrf -> csrf.disable())

                // URL별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입, 로그인, 로그아웃은 인증 없이 접근 허용
                        .requestMatchers("/user/signup", "/user/login", "/user/logout", "/api/**", "/static/chat-client.html", "/ws/**",             // ✅ WebSocket 허용
                                "/topic/**",          // ✅ 메시지 브로커 허용
                                "/app/**",            // ✅ STOMP 송신 허용
                                "/chat-client.html",  // ✅ 클라이언트 페이지 접근 허용
                                "/css/**", "/js/**", "/images/**" ).permitAll()

                        // 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // HTTP Basic 비활성화 (JWT 사용 예정)
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}