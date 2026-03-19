package com.save_help.Save_Help.nationalSubsidy.kafka.topic;

public final class KafkaTopics {
    private KafkaTopics() {}

    public static final String DOMAIN = "nationalSubsidy";
    public static final String V1 = ".v1";

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

    public static final String SUBSIDY_OPENED_DLT = "subsidy.opened.DLT";
    public static final String USER_ELIGIBILITY_UPDATED_DLT = "user.eligibility.updated.DLT";

    public static final String SUBSIDY_ELIGIBILITY_UPDATED_DLQ = "subsidy.eligibility.updated.dlq";
    public static final String SUBSIDY_ELIGIBILITY_UPDATED_DLT = "subsidy.eligibility.updated.dlt";
    public static final String SUBSIDY_OPENED = "subsidy.opened";
    public static final String SUBSIDY_ACTIVATED = "subsidy.activated";

    public static final String SUBSIDY_SUBSCRIPTION_CREATED  = "subsidy.subscription.created.v1";
    public static final String SUBSIDY_SUBSCRIPTION_CANCELED = "subsidy.subscription.canceled.v1";

    public static final String SUBSIDY_APPLICATION_CREATED = "subsidy.application.created.v1";
    public static final String SUBSIDY_APPLICATION_STATUS = "subsidy.application.status.v1";
    public static final String SUBSIDY_NOTIFICATION_REQUESTED = "subsidy.notification.requested.v1";
    public static final String SUBSIDY_AUTO_APPLY_REQUESTED = "subsidy.auto.apply.requested.v1";
    public static final String SUBSIDY_AUTO_APPLY_MATCHED = "subsidy.auto.apply.matched.v1";
    public static final String SUBSIDY_AUTO_APPLY_CREATED = "subsidy.auto.apply.created.v1";

    // 생필품 관련 topic
    public static final String NECESSITY_AUTO_APPLY_REQUESTED = "necessity.auto.apply.requested.v1";
    public static final String NECESSITY_AUTO_APPLY_VALIDATED = "necessity.auto.apply.validated.v1";
    public static final String NECESSITY_AUTO_APPLY_CREATED = "necessity.auto.apply.created.v1";
    public static final String NECESSITY_AUTO_APPLY_COMPLETED = "necessity.auto.apply.completed.v1";

    public static final String NECESSITY_AUTO_APPLY_REQUEST = "necessity.auto.apply.request.v1";
    public static final String NECESSITY_AUTO_APPLY_RETRY   = "necessity.auto.apply.retry.v1";
    public static final String NECESSITY_AUTO_APPLY_DLQ     = "necessity.auto.apply.dlq.v1";


    public static final String DAILY_NECESSITIES_AUTO_APPLY_COMPLETED =
            "dailynecessities.auto-apply.completed.v1";

    public static final String DAILY_NECESSITIES_AUTO_APPLY_FAILED =
            "dailynecessities.auto-apply.failed.v1";

    public static final String DAILY_NECESSITIES_NOTIFICATION_DLQ =
            "dailynecessities.notification.dlq.v1";
    public static final String DAILY_NECESSITIES_STOCK_LOW = "dailynecessities.stock.low.v1";
    public static final String AUTO_APPLY_COMPLETED = "dailynecessities.auto-apply.completed.v1";
    public static final String AUTO_APPLY_FAILED = "dailynecessities.auto-apply.failed.v1";


    public static final String SUBSIDY_APPLICATION_DLQ = "subsidy.application.dlq.v1";
    public static final String SUBSIDY_NOTIFICATION_DLQ = "subsidy.notification.dlq.v1";
    public static final String SUBSIDY_AUTO_APPLY_DLQ = "subsidy.auto.apply.dlq.v1";

    public static final String SUBSIDY_UPDATED = "subsidy.updated.v1";
    public static final String SUBSIDY_APPLICATION_SUBMITTED = "subsidy.application.submitted.v1";
    public static final String SUBSIDY_APPLICATION_APPROVED = "subsidy.application.approved.v1";

    public static final String DAILY_NECESSITY_NOTIFICATION_REQUESTED =
            "dailynecessity.notification.requested.v1";

    public static final String DAILY_NECESSITY_NOTIFICATION_SENT =
            "dailynecessity.notification.sent.v1";

    public static final String DAILY_NECESSITY_NOTIFICATION_FAILED =
            "dailynecessity.notification.failed.v1";


    public static final String DAILY_NECESSITIES_AUTO_APPLY_REQUEST = "daily-necessities.auto-apply.request";
    public static final String DAILY_NECESSITIES_AUTO_APPLY_RESULT = "daily-necessities.auto-apply.result";
    public static final String DAILY_NECESSITIES_AUTO_APPLY_DLQ = "daily-necessities.auto-apply.dlq";

}
