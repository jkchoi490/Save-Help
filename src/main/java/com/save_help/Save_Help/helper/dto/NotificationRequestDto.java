package com.save_help.Save_Help.helper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {

    private Long helperId;          // 대상 Helper ID
    private Long emergencyId;       // 관련 긴급상황 ID (선택적)
    private String title;           // 알림 제목
    private String message;         // 알림 내용
    private boolean viaSms = true;  // 문자 발송 여부
    private boolean viaPush = true; // 푸시 발송 여부
}