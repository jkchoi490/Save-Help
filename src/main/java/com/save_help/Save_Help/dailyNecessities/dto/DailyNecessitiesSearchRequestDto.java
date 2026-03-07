package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyNecessitiesSearchRequestDto {
    private Long centerId;
    private DailyNecessitiesCategory category;
    private DailyNecessities.ApprovalStatus approvalStatus;
    private Boolean active;
    private String keyword;
}