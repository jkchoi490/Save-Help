package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.AssignmentStatus;
import com.save_help.Save_Help.helper.entity.AssignmentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HelperAssignmentResponseDto {
    private Long assignmentId;
    private Long helperId;
    private String helperName;
    private String helperRole;
    private Long emergencyId;
    private String emergencyTitle;
    private AssignmentType assignmentType;
    private AssignmentStatus status;
    private LocalDateTime assignedAt;
}