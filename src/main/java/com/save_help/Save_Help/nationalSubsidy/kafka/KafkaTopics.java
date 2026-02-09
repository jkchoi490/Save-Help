package com.save_help.Save_Help.nationalSubsidy.kafka;

import java.util.Set;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String SUBSIDY_APPLY_REQUEST = "subsidy.apply.request.v1";
    public static final String SUBSIDY_APPLY_RETRY   = "subsidy.apply.retry.v1";
    public static final String SUBSIDY_APPLY_DLQ     = "subsidy.apply.dlq.v1";
    public static final String SUBSIDY_APP_STATUS    = "subsidy.application.status.v1";

    public static final String SUBSIDY_CREATED = "subsidy.created";
    public static final String USER_ELIGIBILITY_APPLIED = "user-eligibility-applied";
    public static final String USER_CREATED = "user.created";
    public static final String USER_ELIGIBILITY_UPDATED = "user.eligibility.updated";

    public static final String SUBSIDY_CREATED_DLQ = "subsidy.created.dlq";
    public static final String USER_CREATED_DLQ = "user.created.dlq";
    public static final String USER_ELIGIBILITY_UPDATED_DLQ = "user.eligibility.updated.dlq";
    public static final String NATIONALSUBSIDY_SCHEDULED = "nationalSubsidy.scheduled";
}
