package com.save_help.Save_Help.nationalSubsidy.kafka;

public enum ApplicationStatus {
    MATCHED,
    REQUESTED,
    SUBMITTED,
    APPROVED,
    REJECTED,
    APPLIED,      // 신청내역 DB 기록
    PENDING, // 처리중인 상태
    ACCEPTED,        // Kafka 소비 수신 완료
    PROCESSING,      // 워커가 실제 처리 시작
    PERSISTED       // DB 반영 완료

}