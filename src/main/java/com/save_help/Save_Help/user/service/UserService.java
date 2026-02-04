package com.save_help.Save_Help.user.service;

import com.save_help.Save_Help.nationalSubsidy.kafka.UserCreatedInternalEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.UserEligibilityChangedInternalEvent;
import com.save_help.Save_Help.user.dto.LoginRequestDto;
import com.save_help.Save_Help.user.dto.SignUpRequestDto;
import com.save_help.Save_Help.user.dto.TokenResponseDto;
import com.save_help.Save_Help.user.entity.Provider;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import com.save_help.Save_Help.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

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
                .provider(Provider.LOCAL) // 여기 필수
                .providerUserId(requestDto.getProviderUserId())
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

    @Transactional
    public Long create(User u) {
        User saved = userRepository.save(u);
        publisher.publishEvent(new UserCreatedInternalEvent(saved.getId()));
        return saved.getId();
    }

    /**
     * 나이/소득/건강 상태/긴급 상태 변경 시 자동신청 재실행을 위한 이벤트 발행
     * - 변경이 실제로 있을 때만 발행
     */
    @Transactional
    public Long updateEligibility(Long userId, Integer age, String incomeLevel, Boolean disabled, Boolean inEmergency) {
        User user = userRepository.findById(userId).orElseThrow();

        int oldAge = user.getAge();
        String oldIncome = user.getIncomeLevel();
        boolean oldDisabled = user.isDisabled();
        boolean oldEmergency = user.isInEmergency();

        if (age != null) user.setAge(age);
        if (incomeLevel != null) user.setIncomeLevel(incomeLevel);
        if (disabled != null) user.setDisabled(disabled);
        if (inEmergency != null) user.setInEmergency(inEmergency);

        userRepository.save(user);

        Set<String> changed = new HashSet<>();
        if (oldAge != user.getAge()) changed.add("age");
        if (!Objects.equals(oldIncome, user.getIncomeLevel())) changed.add("incomeLevel");
        if (oldDisabled != user.isDisabled()) changed.add("disabled");
        if (oldEmergency != user.isInEmergency()) changed.add("inEmergency");

        if (!changed.isEmpty()) {
            publisher.publishEvent(new UserEligibilityChangedInternalEvent(userId, changed));
        }

        return userId;
    }


}