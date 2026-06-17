package com.save_help.Save_Help.hospital.dto;

import com.save_help.Save_Help.hospital.entity.Hospital;
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

    public static HospitalResponseDto fromEntity(Hospital hospital) {

        if (hospital == null) {
            return null;
        }

        return HospitalResponseDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .type(hospital.getType())
                .phoneNumber(hospital.getPhoneNumber())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .active(hospital.isActive())
                .bedCount(hospital.getBedCount())
                .remainingBeds(hospital.getBedCount())
                .build();
    }
}
