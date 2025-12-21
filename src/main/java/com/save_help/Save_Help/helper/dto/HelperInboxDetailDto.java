package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.emergency.entity.EmergencySeverity;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.helper.entity.AssignmentProgressStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HelperInboxDetailDto {
    private Long assignmentId;

    private Long emergencyId;
    private String title;
    private String description;
    private String location;
    private Double latitude;
    private Double longitude;

    private EmergencySeverity severity;
    private EmergencyStatus emergencyStatus;
    private LocalDateTime requestedAt;

    private Long requesterId;
    private String requesterName;

    private AssignmentProgressStatus progressStatus;
    private String memo;

    private LocalDateTime assignedAt;
    private LocalDateTime acceptedAt;
    private LocalDateTime completedAt;
    private LocalDateTime updatedAt;
}
