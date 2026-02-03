package com.save_help.Save_Help.nationalSubsidy.kafka.messages;

public record SubsidyApplyRequest(
        String eventId,
        long occurredAt,
        String traceId,
        long userId,
        long subsidyId,
        long applicationId,
        String idempotencyKey,
        int priority,
        String reason,
        int attempt
) {}