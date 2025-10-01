package com.save_help.Save_Help.user.controller;

import com.save_help.Save_Help.user.dto.LoginRequestDto;
import com.save_help.Save_Help.user.dto.SignUpRequestDto;
import com.save_help.Save_Help.user.dto.TokenResponseDto;
import com.save_help.Save_Help.user.service.UserService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입에 성공하였습니다");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseEntity<Void>> logout() {
        // 클라이언트에서 로그아웃, 성공 응답
        return ResponseEntity.ok(null);
    }


}
