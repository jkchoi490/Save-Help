package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
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
    private String centerName;

    public DailyNecessitiesDto() {}

    public DailyNecessitiesDto(Long id, String name, NecessityCategory category,
                               String unit, Integer stock, LocalDate expirationDate,
                               Long centerId, String centerName) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.stock = stock;
        this.expirationDate = expirationDate;
        this.centerId = centerId;
        this.centerName = centerName;
    }

    // Entity → DTO 변환 메서드 추가
    public static DailyNecessitiesDto fromEntity(DailyNecessities entity) {
        return new DailyNecessitiesDto(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getUnit(),
                entity.getStock(),
                entity.getExpirationDate(),
                entity.getProvidedBy() != null ? entity.getProvidedBy().getId() : null,
                entity.getProvidedBy() != null ? entity.getProvidedBy().getName() : null
        );
    }
}