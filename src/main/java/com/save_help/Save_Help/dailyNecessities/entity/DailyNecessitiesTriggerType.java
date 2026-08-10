package com.save_help.Save_Help.dailyNecessities.entity;

public enum DailyNecessitiesTriggerType {
    USER_CREATED, // 사용자가 새로 가입해서 최초 자격 검사를 실행
    USER_DATA_UPDATED, // 사용자 일반 데이터가 변경되어 다시 검사
    USER_ELIGIBILITY_UPDATED, // 소득, 복지 유형, 장애 여부 등 신청 자격 데이터가 변경됨
    AUTO_APPLY_ENABLED, // 사용자가 자동 신청에 새로 동의함
    MANUAL_RECHECK // 관리자나 사용자가 수동으로 재검사를 요청하는 경우
}
