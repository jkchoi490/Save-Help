package com.save_help.Save_Help.emergency.dto;

import com.save_help.Save_Help.emergency.entity.EmergencySeverity;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmergencyVoiceCreateResponseDto {
    private Long emergencyId;
    private Long voiceNoteId;
    private EmergencyStatus status;
    private EmergencySeverity severity;
    private String description;
    private LocalDateTime requestedAt;
}