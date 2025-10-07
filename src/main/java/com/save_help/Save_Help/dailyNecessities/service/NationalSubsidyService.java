package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.dto.NationalSubsidyResponseDto;
import com.save_help.Save_Help.dailyNecessities.repository.NationalSubsidyRepository;
import com.save_help.Save_Help.nationalSubsidy.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.SubsidyType;
import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NationalSubsidyService {

    private final NationalSubsidyRepository repository;

    public NationalSubsidyResponseDto create(NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = new NationalSubsidy();
        updateEntityFromDto(subsidy, dto);
        NationalSubsidy saved = repository.save(subsidy);
        return toResponseDto(saved);
    }

    public List<NationalSubsidyResponseDto> findAll() {
        return repository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto findById(Long id) {
        NationalSubsidy subsidy = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        return toResponseDto(subsidy);
    }

    public List<NationalSubsidyResponseDto> findByType(SubsidyType type) {
        return repository.findByType(type).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<NationalSubsidyResponseDto> searchByName(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto update(Long id, NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        updateEntityFromDto(subsidy, dto);
        return toResponseDto(subsidy);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void updateEntityFromDto(NationalSubsidy subsidy, NationalSubsidyRequestDto dto) {
        subsidy.setName(dto.getName());
        subsidy.setDescription(dto.getDescription());
        subsidy.setType(dto.getType());
        subsidy.setCenter(dto.getCenter());
        subsidy.setMaxAmount(dto.getMaxAmount());
        subsidy.setTargetGroup(dto.getTargetGroup());
        subsidy.setStartDate(dto.getStartDate());
        subsidy.setEndDate(dto.getEndDate());
        subsidy.setActive(dto.isActive());
        subsidy.setMinAge(dto.getMinAge());
        subsidy.setMaxAge(dto.getMaxAge());
        subsidy.setIncomeLevel(dto.getIncomeLevel());
        subsidy.setDisabilityRequired(dto.getDisabilityRequired());
        subsidy.setEmergencyOnly(dto.getEmergencyOnly());
    }

    private NationalSubsidyResponseDto toResponseDto(NationalSubsidy s) {
        return NationalSubsidyResponseDto.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .type(s.getType())
                .center(s.getCenter())
                .maxAmount(s.getMaxAmount())
                .targetGroup(s.getTargetGroup())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .active(s.isActive())
                .minAge(s.getMinAge())
                .maxAge(s.getMaxAge())
                .incomeLevel(s.getIncomeLevel())
                .disabilityRequired(s.getDisabilityRequired())
                .emergencyOnly(s.getEmergencyOnly())
                .build();
    }
}