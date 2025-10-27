package com.save_help.Save_Help.helper.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelperRecommendationDto {
    private Long id;
    private String name;
    private String role;
    private Double latitude;
    private Double longitude;
    private Double distanceKm; // 요청 위치와의 거리
}
