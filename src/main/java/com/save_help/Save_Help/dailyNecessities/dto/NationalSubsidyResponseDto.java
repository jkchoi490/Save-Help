package com.save_help.Save_Help.dailyNecessities.dto;

import com.save_help.Save_Help.nationalSubsidy.SubsidyType;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class NationalSubsidyResponseDto {
    private Long id;
    private String name;
    private String description;
    private SubsidyType type;
    private String center;
    private Integer maxAmount;
    private String targetGroup;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private Integer minAge;
    private Integer maxAge;
    private String incomeLevel;
    private Boolean disabilityRequired;
    private Boolean emergencyOnly;
}
