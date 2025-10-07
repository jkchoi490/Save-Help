package com.save_help.Save_Help.hospital.dto;

import com.save_help.Save_Help.hospital.entity.HospitalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HospitalRequestDto {
    private String name;
    private HospitalType type;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private boolean active;
    private int bedCount;
}
