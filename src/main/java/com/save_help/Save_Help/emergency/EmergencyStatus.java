package com.save_help.Save_Help.emergency;

public enum EmergencyStatus {
    PENDING,    // 요청됨 (아직 처리 안 됨)
    IN_PROGRESS,// 처리 중
    RESOLVED,   // 처리 완료
    CANCELLED   // 사용자 취소
}
