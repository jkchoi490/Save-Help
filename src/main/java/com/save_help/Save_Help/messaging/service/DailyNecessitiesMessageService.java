package com.save_help.Save_Help.messaging.service;

import com.save_help.Save_Help.messaging.entity.DailyNecessitiesMessage;
import com.save_help.Save_Help.messaging.entity.DailyNecessitiesMessageSession;
import com.save_help.Save_Help.messaging.repository.DailyNecessitiesMessageRepository;
import com.save_help.Save_Help.messaging.repository.DailyNecessitiesMessageSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesMessageService {

    private final DailyNecessitiesMessageSessionRepository sessionRepository;
    private final DailyNecessitiesMessageRepository messageRepository;
    private final TwilioService twilioService;

    @Transactional
    public void handleIncomingSms(String from, String to, String body) {
        // 기존 세션 확인 또는 생성
        DailyNecessitiesMessageSession session = sessionRepository.findByUserPhoneNumber(from)
                .orElseGet(() -> {
                    DailyNecessitiesMessageSession newSession = new DailyNecessitiesMessageSession();
                    newSession.setUserPhoneNumber(from);
                    newSession.setCenterPhoneNumber(to);
                    return sessionRepository.save(newSession);
                });

        // 메시지 저장
        DailyNecessitiesMessage message = new DailyNecessitiesMessage();
        message.setSession(session);
        message.setSender("USER");
        message.setContent(body);
        messageRepository.save(message);

        // 센터에게 알림
        System.out.println("[SMS 알림] 사용자(" + from + ")의 요청: " + body);
    }

    @Transactional
    public void sendReply(Long sessionId, String content) {
        DailyNecessitiesMessageSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("세션이 존재하지 않습니다."));

        // Twilio API로 사용자에게 메시지 발송
        twilioService.sendSms(session.getUserPhoneNumber(), content);

        // DB 저장
        DailyNecessitiesMessage message = new DailyNecessitiesMessage();
        message.setSession(session);
        message.setSender("CENTER");
        message.setContent(content);
        messageRepository.save(message);
    }

    public List<DailyNecessitiesMessage> getMessages(Long sessionId) {
        return messageRepository.findBySessionIdOrderBySentAtAsc(sessionId);
    }
}