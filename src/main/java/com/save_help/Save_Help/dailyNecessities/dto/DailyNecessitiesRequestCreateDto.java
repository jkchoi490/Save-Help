package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.Getter;

@Getter
public class DailyNecessitiesRequestCreateDto {
    private Long userId;

    // 특정 센터 지정(없으면 전체)
    private Long centerId;

    // 특정 품목 지정(없으면 itemName 입력)
    private Long itemId;
    private String itemName;

    private Integer quantity;
    private String message;

    private String contact;
    private String address;

    private Integer priority; // 1~5 (optional)
}
