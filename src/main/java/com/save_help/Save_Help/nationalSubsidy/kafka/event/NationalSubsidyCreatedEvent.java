package com.save_help.Save_Help.nationalSubsidy.kafka.event;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NationalSubsidyCreatedEvent {

    private String eventId;

    private Long subsidyId;

    private LocalDateTime occurredAt;
}