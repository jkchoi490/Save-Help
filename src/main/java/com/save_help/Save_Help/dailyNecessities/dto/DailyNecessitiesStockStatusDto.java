package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyNecessitiesStockStatusDto {

    private Long itemId;

    private String name;

    private int stock;

    private int safetyStock;

    private boolean lowStock;

}