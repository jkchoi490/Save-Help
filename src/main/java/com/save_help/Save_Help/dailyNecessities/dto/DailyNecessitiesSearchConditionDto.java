package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessitiesSearchConditionDto {

    private Long centerId;
    private DailyNecessitiesCategory category;
    private DailyNecessities.ApprovalStatus approvalStatus;
    private Boolean active;

    // 키워드(품목명 등)
    private String keyword;

    // 옵션: 재고 필터까지 같이 받고 싶으면 확장
    private Integer stockGte;
    private Integer stockLte;
}