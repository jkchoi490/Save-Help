package com.save_help.Save_Help.hospital.dto;

import com.save_help.Save_Help.hospital.entity.HospitalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HospitalResponseDto {
    private Long id;
    private String name;
    private HospitalType type;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private boolean active;
    private int bedCount;
    private int remainingBeds; // 남은 병상 수
}
