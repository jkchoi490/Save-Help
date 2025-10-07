package com.save_help.Save_Help.nationalSubsidy.dto;

import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class NationalSubsidyRequestDto {
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
