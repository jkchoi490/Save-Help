package com.save_help.Save_Help.nationalSubsidy.kafka.event;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubsidyApplicationCreatedEvent {

    private Long applicationId;
    private Long userId;
    private Long subsidyId;
    private String eventId;
    private Long occurredAt;
}