package com.save_help.Save_Help.nationalSubsidy.dto;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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

    public static NationalSubsidyResponseDto fromEntity(NationalSubsidy entity) {
        NationalSubsidyResponseDto dto = new NationalSubsidyResponseDto(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getType(),
                entity.getCenter(),
                entity.getMaxAmount(),
                entity.getTargetGroup(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.isActive(),
                entity.getMinAge(),
                entity.getMaxAge(),
                entity.getIncomeLevel(),
                entity.getDisabilityRequired(),
                entity.getEmergencyOnly()
        );
        return dto;
    }

}
