package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyNecessitiesContactRequestCreateDto {
    private Long userId;
    private String contactPhone;
    private String message;
}