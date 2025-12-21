package com.save_help.Save_Help.helper.entity;

public enum AssignmentProgressStatus {
    ASSIGNED,     // 배정됨(대기)
    ACCEPTED,     // 헬퍼 수락
    IN_PROGRESS,  // 처리 중(현장 이동/조치 중)
    COMPLETED,    // 완료
    CANCELLED     // 취소
}
