package com.save_help.Save_Help.helper.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HelperLocationResponseDto {
    private Long id;
    private String name;
    private String role;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
}