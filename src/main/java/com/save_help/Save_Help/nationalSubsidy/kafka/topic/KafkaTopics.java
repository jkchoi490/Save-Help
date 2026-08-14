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
    public static final String DAILY_NECESSITY_CREATED = "dailynecessity.created.v1";
    public static final String DAILY_NECESSITY_APPROVAL_REQUESTED = "dailynecessity.approval.requested.v1";
    public static final String DAILY_NECESSITY_APPROVED = "dailynecessity.approved.v1";
    public static final String DAILY_NECESSITY_CREATE = "daily-necessities.created.v2";
    public static final String DAILY_NECESSITY_ELIGIBILITY_CHANGED =
            "daily-necessity-eligibility-changed";

    // 생필품 생성 이벤트 Topic
    public static final String DAILY_NECESSITIES_CREATED =
            "daily.necessities.created";

    // 생필품 승인됨
    public static final String DAILY_NECESSITIES_APPROVED =
            "daily.necessity.approved";

    // 자동신청 성공
    public static final String DAILY_NECESSITIES_AUTO_APPLY_SUCCESS =
            "necessity.auto.apply.success";

    // 자동신청 알림 요청
    public static final String DAILY_NECESSITIES_AUTO_APPLY_NOTIFICATION_REQUEST =
            "necessity.auto.apply.notification.request";

    // 자동신청 저장 성공
    public static final String DAILY_NECESSITIES_AUTO_APPLY_SAVED =
            "necessity.auto.apply.saved";

    // 자동신청 배정 완료
    public static final String DAILY_NECESSITIES_AUTO_APPLY_ALLOCATED =
            "necessity.auto.apply.allocated";

    // 신청 승인 완료
    public static final String DAILY_NECESSITIES_APPLICATION_APPROVED =
            "dailynecessities.application.approved";

    public static final String HOSPITAL_BEDCOUNT_UPDATED =
            "hospital.bed.updated";

    // 자동신청 기준 검증 성공
    public static final String DAILY_NECESSITIES_AUTO_APPLY_VALIDATE_SUCCESS =
            "dailynecessities.auto-apply.validate.success";

    public static final String DAILY_NECESSITIES_AUTO_APPLY_APPROVED =
            "dailynecessities.auto-apply.approved";

    // 생필품 신청 생성
    public static final String DAILY_NECESSITIES_APPLICATION_CREATED =
            "dailynecessities.application.created";

    public static final String DAILY_NECESSITIES_CREATED_DLQ =
            "dailynecessities.created.dlq";

    public static final String DAILY_NECESSITIES_STOCK_INCREASED =
            "dailynecessities.stock.increased";

    // 국가 보조금 신청 시작
    public static final String NATIONAL_SUBSIDY_OPENED =
            "subsidy.opened.v1";

    // 국가보조금 신청 상태 이벤트
    public static final String NATIONAL_SUBSIDY_APPLICATION_PENDING =
            "subsidy.application.pending";

    // 국가보조금 생성
    public static final String NATIONAL_SUBSIDY_CREATED =
            "subsidy.created";

    // 국가보조금 신청 완료
    public static final String NATIONAL_SUBSIDY_APPLICATION_APPLIED =
            "subsidy.applied";

    // 사용자 자동 신청 자격 확인 완료
    public static final String USER_ELIGIBILITY_COMPLETED =
            "user.eligibility.completed.v1";

    public static final String DAILY_NECESSITIES_AUTO_APPLY_DLT =
            "daily-necessity.auto-apply.request.v1-dlt";

    // 사용자가 생필품 신청 요건에 적합한 지 확인
    public static final String DAILY_NECESSITIES_USER_ELIGIBILITY_CHECK =
            "daily-necessities.user-eligibility.check.v1";



}
