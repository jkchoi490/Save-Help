package com.save_help.Save_Help.nationalSubsidy.kafka.event;

import java.time.LocalDateTime;

public record SubsidyEligibilityUpdatedEvent(String eventId, LocalDateTime occurredAt, Long subsidyId) {}