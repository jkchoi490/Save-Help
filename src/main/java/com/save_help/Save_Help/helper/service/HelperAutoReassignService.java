package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import com.save_help.Save_Help.helper.entity.HelperAssignment;
import com.save_help.Save_Help.helper.repository.HelperAssignmentRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HelperAutoReassignService {

    private final HelperRepository helperRepository;
    private final HelperAssignmentRepository assignmentRepository;
    private final HelperNotificationService notificationService;

    /**
     * 응답하지 않은 헬퍼를 감지하고 자동 재배치 수행
     */
    @Async
    public void reassignIfUnresponsive(Long emergencyId) {
        Optional<HelperAssignment> assignmentOpt = assignmentRepository.findByEmergencyId(emergencyId);

        if (assignmentOpt.isEmpty()) return;
        HelperAssignment assignment = assignmentOpt.get();
        Helper currentHelper = assignment.getHelper();

        // 1. 일정 시간 내 응답이 없으면 재배정
        if (!isHelperResponsive(currentHelper)) {
            Helper newHelper = helperRepository.findFirstByCommunityCenter_IdAndAvailableTrueOrderByIdAsc(currentHelper.getCommunityCenter().getId())
                    .orElseThrow(() -> new IllegalStateException("대체 가능한 헬퍼가 없습니다."));

            assignment.setHelper(newHelper);
            assignmentRepository.save(assignment);

            notificationService.notifyHelper(newHelper.getId(),
                    "새로운 긴급 상황이 자동 지정되었습니다. 빠르게 출동해주세요!");
            notificationService.notifyAdmins(
                    String.format("헬퍼 %d가 응답하지 않아, 헬퍼 %d로 재배치되었습니다.", currentHelper.getId(), newHelper.getId())
            );

            log.info("응답 없음: 헬퍼 {} → 헬퍼 {}로 자동 재배치 완료", currentHelper.getId(), newHelper.getId());
        }
    }

    private boolean isHelperResponsive(Helper helper) {
        // TODO: 실제로 최근 상태 업데이트 시간 기준으로 판단 (예: 30초 이내 상태 변경 여부)
        return helper.getActivityStatus() == HelperActivityStatus.RESPONDING;
    }
}
