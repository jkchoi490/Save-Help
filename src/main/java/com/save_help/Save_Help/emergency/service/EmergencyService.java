package com.save_help.Save_Help.emergency.service;


import com.save_help.Save_Help.emergency.dto.EmergencyRequestDto;
import com.save_help.Save_Help.emergency.dto.EmergencyResponseDto;
import com.save_help.Save_Help.emergency.dto.EmergencyVoiceCreateRequestDto;
import com.save_help.Save_Help.emergency.dto.EmergencyVoiceCreateResponseDto;
import com.save_help.Save_Help.emergency.entity.*;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.emergency.repository.EmergencyVoiceNoteRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmergencyService {

    private final EmergencyRepository emergencyRepository;
    private final EmergencyVoiceNoteRepository voiceNoteRepository;
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


    @Transactional
    public EmergencyVoiceCreateResponseDto createEmergencyByVoice(EmergencyVoiceCreateRequestDto req) {

        // 1) transcript 검증 (필수)
        String transcript = normalize(req.getTranscript());
        if (!StringUtils.hasText(transcript)) {
            throw new IllegalArgumentException("transcript is required.");
        }
        if (transcript.length() > 2000) {
            throw new IllegalArgumentException("transcript is too long. max=2000");
        }

        // 2) requester 검증
        if (req.getRequesterId() == null) {
            throw new IllegalArgumentException("requesterId is required (replace with auth later).");
        }
        User requester = userRepository.findById(req.getRequesterId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + req.getRequesterId()));

        // 3) Emergency 생성
        Emergency emergency = new Emergency();
        emergency.setRequester(requester);
        emergency.setTitle(StringUtils.hasText(req.getTitle()) ? req.getTitle().trim() : "음성 긴급 요청");
        emergency.setDescription(transcript);
        emergency.setLatitude(req.getLatitude());
        emergency.setLongitude(req.getLongitude());
        emergency.setLocation(req.getLocation());
        emergency.setStatus(EmergencyStatus.REQUESTED); // 너 enum에 맞게 조정
        emergency.setRequestedAt(LocalDateTime.now());
        emergency.setResolved(false);

        // severity: 요청에 있으면 사용, 없으면 기본값 or 텍스트 기반 추론
        EmergencySeverity severity = req.getSeverity() != null ? req.getSeverity() : EmergencySeverity.MEDIUM;
        emergency.setSeverity(severity);

        emergencyRepository.save(emergency);

        // 4) VoiceNote 저장
        EmergencyVoiceNote note = new EmergencyVoiceNote();
        note.setEmergency(emergency);
        note.setTranscript(transcript);
        note.setSource(VoiceInputSource.CLIENT_STT);
        voiceNoteRepository.save(note);

        return EmergencyVoiceCreateResponseDto.builder()
                .emergencyId(emergency.getId())
                .voiceNoteId(note.getId())
                .status(emergency.getStatus())
                .severity(emergency.getSeverity())
                .description(emergency.getDescription())
                .requestedAt(emergency.getRequestedAt())
                .build();
    }

    private String normalize(String s) {
        if (s == null) return null;
        return s.trim().replaceAll("\\s+", " ");
    }
}

