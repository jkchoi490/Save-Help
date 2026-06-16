package com.save_help.Save_Help.nationalSubsidy.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HospitalBedCountEvent {

    private Long hospitalId;
    private Integer remainingBeds;
    private Long occurredAt;
}