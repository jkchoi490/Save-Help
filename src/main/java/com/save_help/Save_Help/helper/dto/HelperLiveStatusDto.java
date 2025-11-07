package com.save_help.Save_Help.helper.dto;

import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HelperLiveStatusDto {

    private Long id;
    private String name;
    private String role;
    private HelperActivityStatus activityStatus; // 근무 중 / 이동 중 / 출동 중 등
    private Double latitude;
    private Double longitude;
    private String currentTask; // 현재 수행 중인 업무(응급 ID 등)

    public static HelperLiveStatusDto fromEntity(Helper helper) {
        return HelperLiveStatusDto.builder()
                .id(helper.getId())
                .name(helper.getName())
                .role(helper.getRole().name())
                .activityStatus(helper.getActivityStatus())
                .latitude(helper.getLatitude())
                .longitude(helper.getLongitude())
                .currentTask(helper.getCurrentTask())
                .build();
    }
}
