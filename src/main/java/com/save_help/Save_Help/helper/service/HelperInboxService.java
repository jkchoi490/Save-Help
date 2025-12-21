package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;

import com.save_help.Save_Help.helper.dto.HelperInboxDetailDto;
import com.save_help.Save_Help.helper.dto.HelperInboxItemDto;
import com.save_help.Save_Help.helper.entity.HelperAssignment;
import com.save_help.Save_Help.helper.entity.AssignmentProgressStatus;
import com.save_help.Save_Help.helper.repository.HelperAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HelperInboxService {

    private final HelperAssignmentRepository assignmentRepository;

    public Page<HelperInboxItemDto> list(Long helperId, AssignmentProgressStatus progressStatus, int page, int size) {
        var pageable = PageRequest.of(page, size);

        Page<HelperAssignment> result = (progressStatus == null)
                ? assignmentRepository.findByHelperIdOrderByAssignedAtDesc(helperId, pageable)
                : assignmentRepository.findByHelperIdAndProgressStatusOrderByAssignedAtDesc(helperId, progressStatus, pageable);

        return result.map(this::toListDto);
    }

    public HelperInboxDetailDto detail(Long helperId, Long assignmentId) {
        HelperAssignment a = assignmentRepository.findByIdAndHelperId(assignmentId, helperId)
                .orElseThrow(() -> new IllegalArgumentException("해당 헬퍼의 배정 내역이 아닙니다. assignmentId=" + assignmentId));

        return toDetailDto(a);
    }

    @Transactional
    public void updateMemo(Long helperId, Long assignmentId, String memo) {
        HelperAssignment a = assignmentRepository.findByIdAndHelperId(assignmentId, helperId)
                .orElseThrow(() -> new IllegalArgumentException("해당 헬퍼의 배정 내역이 아닙니다. assignmentId=" + assignmentId));

        a.setMemo(memo);
        a.setUpdatedAt(java.time.LocalDateTime.now());
    }

    private HelperInboxItemDto toListDto(HelperAssignment a) {
        Emergency e = a.getEmergency();

        return HelperInboxItemDto.builder()
                .assignmentId(a.getId())
                .emergencyId(e.getId())
                .title(e.getTitle())
                .location(e.getLocation())
                .severity(e.getSeverity())
                .emergencyStatus(e.getStatus())
                .requesterId(e.getRequester() != null ? e.getRequester().getId() : null)
                // .requesterName(e.getRequester() != null ? e.getRequester().getName() : null) // User에 name 있으면
                .progressStatus(a.getProgressStatus())
                .assignedAt(a.getAssignedAt())
                .build();
    }

    private HelperInboxDetailDto toDetailDto(HelperAssignment a) {
        Emergency e = a.getEmergency();

        return HelperInboxDetailDto.builder()
                .assignmentId(a.getId())
                .emergencyId(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .location(e.getLocation())
                .latitude(e.getLatitude())
                .longitude(e.getLongitude())
                .severity(e.getSeverity())
                .emergencyStatus(e.getStatus())
                .requestedAt(e.getRequestedAt())
                .requesterId(e.getRequester() != null ? e.getRequester().getId() : null)
                // .requesterName(e.getRequester() != null ? e.getRequester().getName() : null)
                .progressStatus(a.getProgressStatus())
                .memo(a.getMemo())
                .assignedAt(a.getAssignedAt())
                .acceptedAt(a.getAcceptedAt())
                .completedAt(a.getCompletedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
