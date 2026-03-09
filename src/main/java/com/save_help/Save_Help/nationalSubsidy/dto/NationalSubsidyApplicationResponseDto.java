package com.save_help.Save_Help.nationalSubsidy.dto;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyApplication;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class NationalSubsidyApplicationResponseDto {

    private Long applicationId;
    private Long userId;
    private Long subsidyId;

    private String subsidyName;
    private String subsidyDescription;
    private String center;
    private Integer maxAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    private NationalSubsidyApplication.Status status;
    private NationalSubsidyApplication.AppliedBy appliedBy;

    private String reason;
    private String eventId;
    private boolean active;

    private LocalDateTime appliedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NationalSubsidyApplicationResponseDto fromEntity(NationalSubsidyApplication app) {
        return NationalSubsidyApplicationResponseDto.builder()
                .applicationId(app.getId())
                .userId(app.getUser().getId())
                .subsidyId(app.getSubsidy().getId())
                .subsidyName(app.getSubsidy().getName())
                .subsidyDescription(app.getSubsidy().getDescription())
                .center(app.getSubsidy().getCenter())
                .maxAmount(app.getSubsidy().getMaxAmount())
                .startDate(app.getSubsidy().getStartDate())
                .endDate(app.getSubsidy().getEndDate())
                .status(app.getStatus())
                .appliedBy(app.getAppliedBy())
                .reason(app.getReason())
                .eventId(app.getEventId())
                .active(app.isActive())
                .appliedAt(app.getAppliedAt())
                .createdAt(app.getCreatedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}