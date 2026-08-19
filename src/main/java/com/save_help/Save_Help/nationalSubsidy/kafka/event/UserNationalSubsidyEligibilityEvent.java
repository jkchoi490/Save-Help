package com.save_help.Save_Help.nationalSubsidy.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserNationalSubsidyEligibilityEvent {

    private String eventId;

    private Long userId;

    private String triggerType;

    private LocalDateTime occurredAt;

}
