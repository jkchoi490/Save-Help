package com.save_help.Save_Help.nationalSubsidy.kafka;

import java.util.Set;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String SUBSIDY_CREATED = "subsidy.created";
    public static final String USER_CREATED = "user.created";
    public static final String USER_ELIGIBILITY_UPDATED = "user.eligibility.updated";

    public static final String SUBSIDY_CREATED_DLQ = "subsidy.created.dlq";
    public static final String USER_CREATED_DLQ = "user.created.dlq";
    public static final String USER_ELIGIBILITY_UPDATED_DLQ = "user.eligibility.updated.dlq";
}

public record SubsidyCreatedEvent(Long subsidyId, String eventId, long occurredAt) {}
public record UserCreatedEvent(Long userId, String eventId, long occurredAt) {}
public record UserEligibilityUpdatedEvent(Long userId, Set<String> changedFields, String eventId, long occurredAt) {}
