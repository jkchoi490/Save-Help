package com.save_help.Save_Help.helper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelperLocationUpdateRequestDto {
    private Long id;         // 헬퍼 ID
    private Double latitude; // 위도
    private Double longitude;// 경도
}