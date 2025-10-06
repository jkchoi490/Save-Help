package com.save_help.Save_Help.emergency.entity;

public enum EmergencyStatus {
    PENDING,    // 요청됨 (아직 처리 안 됨)
    REQUESTED,   // 요청됨
    ASSIGNED,    // 헬퍼나 병원에 배정됨
    IN_PROGRESS,// 처리 중
    RESOLVED,   // 처리 완료
    CANCELLED   // 사용자 취소
}
