package com.save_help.Save_Help.user.controller;

import com.save_help.Save_Help.user.dto.LoginRequestDto;
import com.save_help.Save_Help.user.dto.SignUpRequestDto;
import com.save_help.Save_Help.user.dto.TokenResponseDto;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "Login API", description = "로그인 API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원 가입을 진행합니다")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입에 성공하였습니다");
    }

    @Operation(summary = "로그인", description = "로그인을 합니다")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(userService.login(requestDto));
    }

    @Operation(summary = "로그아웃", description = "로그아웃 합니다")
    @PostMapping("/logout")
    public ResponseEntity<ResponseEntity<Void>> logout() {
        // 클라이언트에서 로그아웃, 성공 응답
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public Long create(@RequestBody User user) {
        return userService.create(user);
    }

    @PatchMapping("/{userId}/eligibility")
    public Long updateEligibility(
            @PathVariable Long userId,
            @RequestBody UpdateEligibilityRequest req
    ) {
        return userService.updateEligibility(userId, req.age, req.incomeLevel, req.disabled, req.inEmergency);
    }

    @Getter @Setter
    public static class UpdateEligibilityRequest {
        public Integer age;
        public String incomeLevel;
        public Boolean disabled;
        public Boolean inEmergency;
    }
}
