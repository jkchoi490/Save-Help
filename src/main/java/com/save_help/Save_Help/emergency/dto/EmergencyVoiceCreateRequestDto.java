package com.save_help.Save_Help.emergency.dto;

import com.save_help.Save_Help.emergency.entity.EmergencySeverity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyVoiceCreateRequestDto {
    private Long requesterId;
    private String title;
    private String transcript;
    private Double latitude;
    private Double longitude;
    private String location;
    private EmergencySeverity severity;
}