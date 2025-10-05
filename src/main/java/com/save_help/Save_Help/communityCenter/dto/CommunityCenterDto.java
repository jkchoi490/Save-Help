package com.save_help.Save_Help.communityCenter.dto;

import com.save_help.Save_Help.communityCenter.entity.CenterType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "커뮤니티 센터 DTO")
public class CommunityCenterDto {

    @Schema(description = "ID (자동 생성)", example = "1")
    private Long id;

    @Schema(description = "센터 이름", example = "서울 강남소방서")
    private String name;

    @Schema(description = "센터 타입 (ex: FIRE_STATION, HOSPITAL, POLICE)", example = "HOSPITAL")
    private CenterType type;

    @Schema(description = "위도", example = "37.4979")
    private Double latitude;

    @Schema(description = "경도", example = "127.0276")
    private Double longitude;

    @Schema(description = "전화번호", example = "02-123-4567")
    private String phoneNumber;

    @Schema(description = "운영 여부", example = "true")
    private boolean active;
}
