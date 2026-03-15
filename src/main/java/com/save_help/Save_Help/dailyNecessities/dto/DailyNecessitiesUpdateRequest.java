package com.save_help.Save_Help.dailyNecessities.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DailyNecessitiesUpdateRequest {
    private String name;
    private Integer stock;
    private String status;
    private Integer price;
}