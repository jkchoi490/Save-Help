package com.save_help.Save_Help.Help.entity;

public enum HelpStatus {
    REQUESTED,     // 요청 생성
    MATCHING,      // 배정/매칭 진행
    IN_PROGRESS,   // 처리 중
    COMPLETED,     // 완료
    CANCELLED      // 취소
}
