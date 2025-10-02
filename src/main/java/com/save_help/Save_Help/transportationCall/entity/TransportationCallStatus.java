package com.save_help.Save_Help.transportationCall.entity;

public enum TransportationCallStatus {
    REQUESTED,   // 호출 요청
    DISPATCHED,  // 차량 배정 / 출발
    ON_ROUTE,    // 이동 중
    COMPLETED,   // 목적지 도착 / 완료
    CANCELLED    // 호출 취소
}