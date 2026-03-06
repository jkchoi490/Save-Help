package com.save_help.Save_Help.nationalSubsidy.entity;

public enum NationalSubsidyApplicationStatus {
    MATCHED,
    REQUESTED,
    SUBMITTED,
    REVIEWING,    // 심사 중
    APPROVED,
    REJECTED,
    APPLIED,      // 신청내역 DB 기록
    PENDING, // 처리중인 상태
    ACCEPTED,        // Kafka 소비 수신 완료
    PROCESSING,      // 워커가 실제 처리 시작
    PERSISTED,       // DB 반영 완료
    VALIDATING,        //검증
    RETRYING,        // 재시도 중
    UNDER_REVIEW,    // 관리자 검토중
    QUEUED,         // Kafka 큐에 적재
    CONSUMED,       // consumer가 읽음
    EVENT_PUBLISHED // 이벤트 발행 완료
}