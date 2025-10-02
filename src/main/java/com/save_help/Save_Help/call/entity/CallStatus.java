package com.save_help.Save_Help.call.entity;

public enum CallStatus {
    REQUESTED,   // 발신자가 요청 보냄
    ACCEPTED,    // 수신자가 수락
    REJECTED,    // 수신자가 거절
    ENDED        // 통화 종료
}
