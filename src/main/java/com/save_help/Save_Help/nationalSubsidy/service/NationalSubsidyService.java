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
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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


    // 보조금 맞춤형 추천
    public List<NationalSubsidyResponseDto> recommend(Integer age, String incomeLevel, Boolean disability, Boolean emergency) {
        List<NationalSubsidy> all = subsidyRepository.findAll();
        return all.stream()
                .filter(s -> s.isActive())
                .filter(s -> (s.getMinAge() == null || age >= s.getMinAge()))
                .filter(s -> (s.getMaxAge() == null || age <= s.getMaxAge()))
                .filter(s -> (incomeLevel == null || s.getIncomeLevel() == null || s.getIncomeLevel().equalsIgnoreCase(incomeLevel)))
                .filter(s -> (disability == null || !s.getDisabilityRequired() || disability))
                .filter(s -> (emergency == null || !s.getEmergencyOnly() || emergency))
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 신청 가능 보조금
    public List<NationalSubsidyResponseDto> findAvailableSubsidies() {
        LocalDate today = LocalDate.now();
        return subsidyRepository.findAvailableSubsidies(today)
                .stream()
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 세부 필터 검색
    public List<NationalSubsidyResponseDto> filter(SubsidyType type, String incomeLevel, Integer minAge,
                                                   Integer maxAge, Boolean disabilityRequired) {
        return subsidyRepository.filter(type, incomeLevel, minAge, maxAge, disabilityRequired)
                .stream()
                .map(NationalSubsidyResponseDto::fromEntity)
                .toList();
    }

    // 유저 신청 내역 조회
    public List<NationalSubsidyResponseDto> findApplicationsByUser(Long userId) {
        List<SubsidyApplication> apps = applicationRepository.findByUser_Id(userId);
        return apps.stream()
                .map(app -> NationalSubsidyResponseDto.fromEntity(app.getSubsidy()))
                .toList();
    }

    // 통계 API (센터별 / 유형별)
    public Map<String, Object> getStatistics() {
        List<NationalSubsidy> all = subsidyRepository.findAll();

        Map<String, Long> byCenter = all.stream()
                .collect(Collectors.groupingBy(NationalSubsidy::getCenter, Collectors.counting()));

        Map<SubsidyType, Long> byType = all.stream()
                .collect(Collectors.groupingBy(NationalSubsidy::getType, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCount", all.size());
        stats.put("byCenter", byCenter);
        stats.put("byType", byType);
        return stats;
    }

    //  CSV Export
    public Resource exportToCsv() {
        List<NationalSubsidy> subsidies = subsidyRepository.findAll();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8)) {

            writer.write("보조금ID,보조금이름,타입,센터명,최대지원금액,보조금지급시작일,보조금지급종료일,보조금활성화여부\n");
            for (NationalSubsidy s : subsidies) {
                writer.write(String.format("%d,%s,%s,%s,%d,%s,%s,%b\n",
                        s.getId(),
                        s.getName(),
                        s.getType(),
                        s.getCenter(),
                        Optional.ofNullable(s.getMaxAmount()).orElse(0),
                        s.getStartDate(),
                        s.getEndDate(),
                        s.isActive()));
            }
            writer.flush();
            return new ByteArrayResource(out.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("CSV 생성 실패", e);
        }
    }

    // 보조금 활성/비활성 상태 변경
    public void updateStatus(Long id, boolean active) {
        NationalSubsidy subsidy = subsidyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 보조금이 존재하지 않습니다. id=" + id));

        subsidy.setActive(active);
        subsidyRepository.save(subsidy);
    }
}