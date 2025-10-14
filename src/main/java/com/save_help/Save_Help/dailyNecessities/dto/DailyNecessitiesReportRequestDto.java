package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyNecessitiesReportRequestDto {
    private Long centerId; // 전체 센터: null
    private LocalDate startDate; // 기간 필터 시작
    private LocalDate endDate;   // 기간 필터 끝
}
