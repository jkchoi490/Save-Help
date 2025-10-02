package com.save_help.Save_Help.user.service;

import com.save_help.Save_Help.user.dto.LoginRequestDto;
import com.save_help.Save_Help.user.dto.SignUpRequestDto;
import com.save_help.Save_Help.user.dto.TokenResponseDto;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import com.save_help.Save_Help.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(SignUpRequestDto requestDto) {
        if (userRepository.existsByLoginId(requestDto.getLoginId())) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
        User user = User.builder()
                .loginId(requestDto.getLoginId())
                .name(requestDto.getName())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .age(requestDto.getAge())
                .gender(requestDto.getGender())
                .build();
        userRepository.save(user);
    }

    // 로그인
    public TokenResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getName());
        return new TokenResponseDto(token);
    }


}