package com.save_help.Save_Help.nationalSubsidy.kafka.messages;

public record SubsidyApplyRetry(
        String eventId,
        long occurredAt,
        String traceId,
        long userId,
        long subsidyId,
        long applicationId,
        String idempotencyKey,
        int attempt,
        long nextRetryAt,          // epoch millis
        String failCode,
        String failMessage
) {}