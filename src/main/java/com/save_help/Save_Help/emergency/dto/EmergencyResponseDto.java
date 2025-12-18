package com.save_help.Save_Help.emergency.dto;

import com.save_help.Save_Help.emergency.entity.Emergency;
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

    public static EmergencyResponseDto from(Emergency emergency) {
        return EmergencyResponseDto.builder()
                .id(emergency.getId())
                .userId(
                        emergency.getRequester() != null
                                ? emergency.getRequester().getId()
                                : null
                )
                .description(emergency.getDescription())
                .latitude(emergency.getLatitude())
                .longitude(emergency.getLongitude())
                .status(emergency.getStatus())
                .requestedAt(emergency.getRequestedAt())
                .resolvedAt(emergency.getResolvedAt())
                .build();
    }
}
