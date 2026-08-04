package com.save_help.Save_Help.dailyNecessities.kafka.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyNecessityEligibilityEvent {

    private Long necessityId;
    private Long centerId;

    private Integer incomeLevel;
    private Boolean requireCheck;

}