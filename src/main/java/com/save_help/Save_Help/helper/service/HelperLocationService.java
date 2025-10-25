package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperLocationResponseDto;
import com.save_help.Save_Help.helper.dto.HelperLocationUpdateRequestDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelperLocationService {

    private final HelperRepository helperRepository;

    public HelperLocationResponseDto updateHelperLocation(HelperLocationUpdateRequestDto dto) {
        Helper helper = helperRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 헬퍼 ID입니다: " + dto.getId()));

        helper.setLatitude(dto.getLatitude());
        helper.setLongitude(dto.getLongitude());
        helper.setLastLocationUpdateTime(LocalDateTime.now());

        helperRepository.save(helper);

        log.info("Helper {} 위치 업데이트 완료: ({}, {})", helper.getId(), dto.getLatitude(), dto.getLongitude());

        // 응답 DTO로 반환
        return HelperLocationResponseDto.builder()
                .id(helper.getId())
                .name(helper.getName())
                .role(String.valueOf(helper.getRole()))
                .phoneNumber(helper.getPhoneNumber())
                .latitude(helper.getLatitude())
                .longitude(helper.getLongitude())
                .build();
    }
}