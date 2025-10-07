package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DailyNecessitiesDto {

    private Long id;
    private String name;
    private NecessityCategory category;
    private String unit;
    private Integer stock;
    private LocalDate expirationDate;
    private Long centerId;

    // 기본 생성자
    public DailyNecessitiesDto() {}

    // 생성자
    public DailyNecessitiesDto(Long id, String name, NecessityCategory category,
                               String unit, Integer stock, LocalDate expirationDate, Long centerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.stock = stock;
        this.expirationDate = expirationDate;
        this.centerId = centerId;
    }


}
