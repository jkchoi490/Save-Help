package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StockStatisticsDto {
    private String name; // 센터명 또는 카테고리명
    private Integer totalStock;
}
