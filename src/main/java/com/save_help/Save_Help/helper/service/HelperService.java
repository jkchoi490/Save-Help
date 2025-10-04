package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.helper.dto.HelperRequestDto;
import com.save_help.Save_Help.helper.dto.HelperResponseDto;
import com.save_help.Save_Help.hospital.repository.HospitalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperService {

    private final HelperRepository helperRepository;
    private final CommunityCenterRepository centerRepository;
    private final HospitalRepository hospitalRepository;

    // 생성
    public HelperResponseDto createHelper(HelperRequestDto dto) {
        CommunityCenter center = dto.getCenterId() != null
                ? centerRepository.findById(dto.getCenterId())
                .orElseThrow(() -> new EntityNotFoundException("Center not found"))
                : null;

        Hospital hospital = dto.getHospitalId() != null
                ? hospitalRepository.findById(dto.getHospitalId())
                .orElseThrow(() -> new EntityNotFoundException("Hospital not found"))
                : null;

        Helper helper = new Helper();
        helper.setName(dto.getName());
        helper.setRole(dto.getRole());
        helper.setCenter(center);
        helper.setHospital(hospital);
        helper.setPhoneNumber(dto.getPhoneNumber());
        helper.setAvailable(dto.isAvailable());

        Helper saved = helperRepository.save(helper);
        return toDto(saved);
    }

    // 단건 조회
    public HelperResponseDto getHelper(Long id) {
        Helper helper = helperRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found"));
        return toDto(helper);
    }

    // 전체 조회
    public List<HelperResponseDto> getAllHelpers() {
        return helperRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 수정
    public HelperResponseDto updateHelper(Long id, HelperRequestDto dto) {
        Helper helper = helperRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Helper not found"));

        if (dto.getName() != null) helper.setName(dto.getName());
        if (dto.getRole() != null) helper.setRole(dto.getRole());
        if (dto.getPhoneNumber() != null) helper.setPhoneNumber(dto.getPhoneNumber());
        helper.setAvailable(dto.isAvailable());

        if (dto.getCenterId() != null) {
            CommunityCenter center = centerRepository.findById(dto.getCenterId())
                    .orElseThrow(() -> new EntityNotFoundException("Center not found"));
            helper.setCenter(center);
        }

        if (dto.getHospitalId() != null) {
            Hospital hospital = hospitalRepository.findById(dto.getHospitalId())
                    .orElseThrow(() -> new EntityNotFoundException("Hospital not found"));
            helper.setHospital(hospital);
        }

        return toDto(helperRepository.save(helper));
    }

    // 삭제
    public void deleteHelper(Long id) {
        helperRepository.deleteById(id);
    }

    // Entity → DTO 변환
    private HelperResponseDto toDto(Helper helper) {
        return HelperResponseDto.builder()
                .id(helper.getId())
                .name(helper.getName())
                .role(helper.getRole())
                .centerId(helper.getCenter() != null ? helper.getCenter().getId() : null)
                .hospitalId(helper.getHospital() != null ? helper.getHospital().getId() : null)
                .phoneNumber(helper.getPhoneNumber())
                .available(helper.isAvailable())
                .build();
    }

    // 역할별 조회
    public List<HelperResponseDto> getHelpersByRole(HelperRole role) {
        return helperRepository.findByRole(role).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // available = true 조회
    public List<HelperResponseDto> getAvailableHelpers() {
        return helperRepository.findByAvailableTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // 역할별 + available = true 조회
    public List<HelperResponseDto> getAvailableHelpersByRole(HelperRole role) {
        return helperRepository.findByRoleAndAvailableTrue(role).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
