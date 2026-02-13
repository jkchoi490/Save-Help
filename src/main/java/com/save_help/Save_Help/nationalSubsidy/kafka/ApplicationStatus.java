package com.save_help.Save_Help.nationalSubsidy.kafka;

public enum ApplicationStatus {
    MATCHED,
    REQUESTED,
    SUBMITTED,
    APPROVED,
    REJECTED,
    APPLIED,      // 신청내역 DB 기록
    PENDING // 처리중인 상태
}