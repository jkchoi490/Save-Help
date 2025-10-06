package com.save_help.Save_Help.emergency.service;


import com.save_help.Save_Help.emergency.dto.EmergencyRequestDto;
import com.save_help.Save_Help.emergency.dto.EmergencyResponseDto;
import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final UserRepository userRepository;

    // 긴급 요청 생성
    public EmergencyResponseDto createEmergency(EmergencyRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 ID입니다."));

        Emergency emergency = new Emergency();
        emergency.setRequester(user);
        emergency.setDescription(dto.getDescription());
        emergency.setLatitude(dto.getLatitude());
        emergency.setLongitude(dto.getLongitude());
        emergency.setStatus(EmergencyStatus.REQUESTED);
        emergency.setRequestedAt(LocalDateTime.now());

        Emergency saved = emergencyRepository.save(emergency);
        return toResponseDto(saved);
    }

    // 전체 조회
    public List<EmergencyResponseDto> getAllEmergencies() {
        return emergencyRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    // 단일 조회
    public EmergencyResponseDto getEmergencyById(Long id) {
        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 긴급 요청을 찾을 수 없습니다."));
        return toResponseDto(emergency);
    }

    // 요청 취소
    public EmergencyResponseDto cancelEmergency(Long id) {
        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 요청을 찾을 수 없습니다."));
        emergency.cancel();
        return toResponseDto(emergencyRepository.save(emergency));
    }

    // 해결 완료
    public EmergencyResponseDto resolveEmergency(Long id) {
        Emergency emergency = emergencyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 요청을 찾을 수 없습니다."));
        emergency.markResolved();
        return toResponseDto(emergencyRepository.save(emergency));
    }

    private EmergencyResponseDto toResponseDto(Emergency e) {
        return EmergencyResponseDto.builder()
                .id(e.getId())
                .userId(e.getRequester().getId())
                .description(e.getDescription())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .status(e.getStatus())
                .requestedAt(e.getRequestedAt())
                .resolvedAt(e.getResolvedAt())
                .build();
    }
}
