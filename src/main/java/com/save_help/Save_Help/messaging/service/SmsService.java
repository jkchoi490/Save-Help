package com.save_help.Save_Help.messaging.service;

import com.save_help.Save_Help.messaging.entity.SmsMessage;
import com.save_help.Save_Help.messaging.entity.SmsSession;
import com.save_help.Save_Help.messaging.repository.SmsMessageRepository;
import com.save_help.Save_Help.messaging.repository.SmsSessionRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final SmsSessionRepository sessionRepository;
    private final SmsMessageRepository messageRepository;

    @Value("${twilio.account-sid}")
    private String ACCOUNT_SID;
    @Value("${twilio.auth-token}")
    private String AUTH_TOKEN;
    @Value("${twilio.phone-number}")
    private String TWILIO_NUMBER;


    // 사용자 → 시스템 (Webhook 수신)
    public void handleIncomingSms(String from, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // 기존 세션이 있는지 확인
        SmsSession session = sessionRepository.findByUserPhoneAndActiveTrue(from)
                .orElseGet(() -> {
                    // 없으면 새 세션 생성
                    SmsSession newSession = SmsSession.builder()
                            .userPhone(from)
                            .helperPhone("") //  번호 예시
                            .startedAt(LocalDateTime.now())
                            .active(true)
                            .build();
                    return sessionRepository.save(newSession);
                });

        // 메시지 저장
        SmsMessage msg = SmsMessage.builder()
                .session(session)
                .sender("USER")
                .content(body)
                .sentAt(LocalDateTime.now())
                .build();
        messageRepository.save(msg);

        // Helper에게 SMS 전송
        Message.creator(
                new PhoneNumber(session.getHelperPhone()),
                new PhoneNumber(TWILIO_NUMBER),
                "[사용자 메시지] " + body
        ).create();

        // 세션 업데이트
        session.setLastMessageAt(LocalDateTime.now());
        sessionRepository.save(session);
    }


    // Helper → 사용자 회신
    public void handleHelperReply(String helperPhone, String toUser, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        // 기존 세션 찾기
        SmsSession session = sessionRepository.findByUserPhoneAndActiveTrue(toUser)
                .orElseThrow(() -> new RuntimeException("세션을 찾을 수 없습니다."));

        // 메시지 저장
        SmsMessage msg = SmsMessage.builder()
                .session(session)
                .sender("HELPER")
                .content(body)
                .sentAt(LocalDateTime.now())
                .build();
        messageRepository.save(msg);

        // 사용자에게 전송
        Message.creator(
                new PhoneNumber(toUser),
                new PhoneNumber(TWILIO_NUMBER),
                "Helper " + body
        ).create();

        // 세션 갱신
        session.setLastMessageAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}