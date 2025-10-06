package com.save_help.Save_Help.emergency.dto;

import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmergencyResponseDto {
    private Long id;
    private Long userId;
    private String description;
    private Double latitude;
    private Double longitude;
    private EmergencyStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime resolvedAt;
}
