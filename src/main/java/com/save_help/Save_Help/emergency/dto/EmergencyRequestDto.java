package com.save_help.Save_Help.emergency.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmergencyRequestDto {
    private Long userId;
    private String description;
    private Double latitude;
    private Double longitude;
}
