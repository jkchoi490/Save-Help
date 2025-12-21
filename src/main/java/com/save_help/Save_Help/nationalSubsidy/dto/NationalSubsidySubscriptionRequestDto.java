package com.save_help.Save_Help.nationalSubsidy.dto;

import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import lombok.Getter;

@Getter
public class NationalSubsidySubscriptionRequestDto {

    private Integer minAge;
    private Integer maxAge;

    private String incomeLevel;

    private Boolean disability;
    private Boolean emergency;

    private SubsidyType type;

    private String region;
}
