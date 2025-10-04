package com.save_help.Save_Help.counseling.service;

import com.save_help.Save_Help.counseling.dto.CounselingRequestDto;
import com.save_help.Save_Help.counseling.dto.CounselingResponseDto;
import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.counseling.repository.CounselingRepository;
import com.save_help.Save_Help.helper.entity.Helper;

import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CounselingService {

    private final CounselingRepository counselingRepository;
    private final UserRepository userRepository;
    private final HelperRepository helperRepository;

    // 상담 등록
    public CounselingResponseDto createCounseling(CounselingRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Helper counselor = helperRepository.findById(dto.getCounselorId())
                .orElseThrow(() -> new EntityNotFoundException("Counselor not found"));

        Counseling counseling = new Counseling();
        counseling.setUser(user);
        counseling.setCounselor(counselor);
        counseling.setStartAt(dto.getStartAt());
        counseling.setEndAt(dto.getEndAt());
        counseling.setStatus(dto.getStatus());
        counseling.setNotes(dto.getNotes());
        counseling.setType(dto.getType());

        Counseling saved = counselingRepository.save(counseling);
        return toDto(saved);
    }

    // 상담 조회 (단건)
    public CounselingResponseDto getCounseling(Long id) {
        Counseling counseling = counselingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Counseling not found"));
        return toDto(counseling);
    }

    // 전체 상담 조회
    public List<CounselingResponseDto> getAllCounselings() {
        return counselingRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 상담 수정
    public CounselingResponseDto updateCounseling(Long id, CounselingRequestDto dto) {
        Counseling counseling = counselingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Counseling not found"));

        if (dto.getStartAt() != null) counseling.setStartAt(dto.getStartAt());
        if (dto.getEndAt() != null) counseling.setEndAt(dto.getEndAt());
        if (dto.getStatus() != null) counseling.setStatus(dto.getStatus());
        if (dto.getNotes() != null) counseling.setNotes(dto.getNotes());
        if (dto.getType() != null) counseling.setType(dto.getType());

        Counseling updated = counselingRepository.save(counseling);
        return toDto(updated);
    }

    // 상담 삭제
    public void deleteCounseling(Long id) {
        counselingRepository.deleteById(id);
    }

    // Entity → DTO 변환
    private CounselingResponseDto toDto(Counseling counseling) {
        return CounselingResponseDto.builder()
                .id(counseling.getId())
                .userId(counseling.getUser().getId())
                .counselorId(counseling.getCounselor().getId())
                .startAt(counseling.getStartAt())
                .endAt(counseling.getEndAt())
                .status(counseling.getStatus())
                .notes(counseling.getNotes())
                .type(counseling.getType())
                .build();
    }
}
