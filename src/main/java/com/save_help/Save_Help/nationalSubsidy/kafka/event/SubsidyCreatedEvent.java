package com.save_help.Save_Help.nationalSubsidy.kafka.event;

public record SubsidyCreatedEvent(
        Long subsidyId,
        String eventId,
        long occurredAt)
{}