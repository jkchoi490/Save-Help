package com.save_help.Save_Help.helper.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationSender {

    // 문자 메시지 전송 (SMS)
    public void sendSms(String phoneNumber, String message) {
        // 실제로는 외부 SMS 서비스(Twilio, Naver Cloud, CoolSMS 등) 연동
        log.info("[SMS] to {} : {}", phoneNumber, message);
    }

    // 푸시 알림 전송 (FCM 등)
    public void sendPush(String title, String message, Long helperId) {
        // 실제로는 Firebase Cloud Messaging 등과 연동
        log.info("[PUSH] to Helper #{} : [{}] {}", helperId, title, message);
    }
}