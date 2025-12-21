package com.save_help.Save_Help.nationalSubsidy.dto;

import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NationalSubsidySubscriptionResponseDto {

    private Long subscriptionId;

    private Integer minAge;
    private Integer maxAge;

    private String incomeLevel;
    private Boolean disability;
    private Boolean emergency;

    private SubsidyType type;
    private String region;

    private boolean active;
    private LocalDateTime createdAt;
}
