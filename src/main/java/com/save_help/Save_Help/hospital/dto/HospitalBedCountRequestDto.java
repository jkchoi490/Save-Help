package com.save_help.Save_Help.hospital.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HospitalBedCountRequestDto {

    @NotNull(message = "병상 수는 필수입니다.")
    private Integer count;
}