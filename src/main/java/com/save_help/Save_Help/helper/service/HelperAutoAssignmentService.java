package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencySeverity;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.dto.HelperAssignmentResponseDto;
import com.save_help.Save_Help.helper.entity.*;
import com.save_help.Save_Help.helper.repository.HelperAssignmentRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HelperAutoAssignmentService {

    private final HelperRepository helperRepository;
    private final HelperAssignmentRepository helperAssignmentRepository;
    private final EmergencyRepository emergencyRepository;

    public HelperAssignmentResponseDto assignHelperBySeverity(Long emergencyId) {
        Emergency emergency = emergencyRepository.findById(emergencyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 Emergency를 찾을 수 없습니다."));

        // 근무 가능자 전체 조회
        List<Helper> availableHelpers = helperRepository.findByAvailableTrue();

        if (availableHelpers.isEmpty()) {
            throw new IllegalStateException("현재 근무 가능한 Helper가 없습니다.");
        }

        // 긴급도 기반 Helper 선택
        Helper bestHelper = selectHelperBySeverity(emergency, availableHelpers);

        // 배정 생성
        HelperAssignment assignment = new HelperAssignment(bestHelper, emergency, AssignmentType.AUTO);
        helperAssignmentRepository.save(assignment);

        // Helper 상태 변경 (배정 중이므로 근무 불가 처리)
        bestHelper.setAvailable(false);

        return HelperAssignmentResponseDto.builder()
                .assignmentId(assignment.getId())
                .helperId(bestHelper.getId())
                .helperName(bestHelper.getName())
                .helperRole(bestHelper.getRole().name())
                .emergencyId(emergency.getId())
                .emergencyTitle(emergency.getTitle())
                .assignmentType(AssignmentType.AUTO)
                .status(assignment.getStatus())
                .assignedAt(assignment.getAssignedAt())
                .build();
    }

    // 긴급도 기반 Helper 선택 로직
    private Helper selectHelperBySeverity(Emergency emergency, List<Helper> candidates) {
        EmergencySeverity severity = emergency.getSeverity();


        return candidates.stream()
                .sorted(Comparator.comparingInt(h -> getHelperPriorityByRole(severity, h.getRole())))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("적합한 Helper를 찾을 수 없습니다."));
    }

    // 긴급도별(임시) 우선순위 테이블
    private int getHelperPriorityByRole(EmergencySeverity severity, HelperRole role) {
        return switch (severity) {
            case CRITICAL -> switch (role) {
                case MEDICAL -> 1;
                case POLICE -> 2;
                case COUNSELOR -> 3;
                default -> 4;
            };
            case HIGH -> switch (role) {
                case POLICE -> 1;
                case MEDICAL -> 2;
                case COUNSELOR -> 3;
                default -> 4;
            };
            case MEDIUM -> switch (role) {
                case COUNSELOR -> 1;
                case MEDICAL -> 2;
                case POLICE -> 3;
                default -> 4;
            };
            case LOW -> 1; // 아무나 가능
        };
    }
}