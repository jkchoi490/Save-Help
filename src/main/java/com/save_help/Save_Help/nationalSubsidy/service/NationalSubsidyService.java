package com.save_help.Save_Help.nationalSubsidy.service;

import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyResponseDto;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyApplication;
import com.save_help.Save_Help.nationalSubsidy.repository.NationalSubsidyRepository;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import com.save_help.Save_Help.nationalSubsidy.dto.NationalSubsidyRequestDto;
import com.save_help.Save_Help.nationalSubsidy.repository.SubsidyApplicationRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NationalSubsidyService {

    private final NationalSubsidyRepository subsidyRepository;
    private final SubsidyApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public NationalSubsidyResponseDto create(NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = new NationalSubsidy();
        updateEntityFromDto(subsidy, dto);
        NationalSubsidy saved = subsidyRepository.save(subsidy);
        return toResponseDto(saved);
    }

    public List<NationalSubsidyResponseDto> findAll() {
        return subsidyRepository.findAll().stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto findById(Long id) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        return toResponseDto(subsidy);
    }

    public List<NationalSubsidyResponseDto> findByType(SubsidyType type) {
        return subsidyRepository.findByType(type).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public List<NationalSubsidyResponseDto> searchByName(String keyword) {
        return subsidyRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(this::toResponseDto)
                .toList();
    }

    public NationalSubsidyResponseDto update(Long id, NationalSubsidyRequestDto dto) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("보조금을 찾을 수 없습니다."));
        updateEntityFromDto(subsidy, dto);
        return toResponseDto(subsidy);
    }

    public void delete(Long id) {
        subsidyRepository.deleteById(id);
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

    public List<NationalSubsidy> findEligibleSubsidies(User user) {
        return subsidyRepository.findAll().stream()
                .filter(NationalSubsidy::isActive)
                .filter(subsidy -> subsidy.getMinAge() == null || user.getAge() >= subsidy.getMinAge())
                .filter(subsidy -> subsidy.getMaxAge() == null || user.getAge() <= subsidy.getMaxAge())
                .filter(subsidy -> subsidy.getIncomeLevel() == null || subsidy.getIncomeLevel().equals(user.getIncomeLevel()))
                .filter(subsidy -> subsidy.getDisabilityRequired() == null || subsidy.getDisabilityRequired().equals(user.isDisabled()))
                .filter(subsidy -> subsidy.getEmergencyOnly() == null || subsidy.getEmergencyOnly().equals(user.isInEmergency()))
                .toList();
    }

    public void autoApplyForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<NationalSubsidy> eligibleSubsidies = findEligibleSubsidies(user);

        for (NationalSubsidy subsidy : eligibleSubsidies) {
            boolean alreadyApplied = applicationRepository
                    .findByUserIdAndSubsidyId(user.getId(), subsidy.getId())
                    .isPresent();

            if (!alreadyApplied) {
                SubsidyApplication application = new SubsidyApplication();
                application.setUser(user);
                application.setSubsidy(subsidy);
                applicationRepository.save(application);
            }
        }
    }

    public void autoApplyForAllUsers() {
        userRepository.findAll().forEach(user -> autoApplyForUser(user.getId()));
    }
}