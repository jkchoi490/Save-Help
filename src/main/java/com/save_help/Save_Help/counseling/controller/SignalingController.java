package com.save_help.Save_Help.counseling.controller;

import com.save_help.Save_Help.counseling.dto.SignalingMessage;
import com.save_help.Save_Help.counseling.service.SignalingSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class SignalingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final SignalingSessionManager sessionManager;

    /***
     *  /app/signal/{sessionId} 로 들어오는 signaling 메시지를 처리
     *  보내는 곳: 사용자 or 상담사
     *  전달받는 곳: 같은 세션의 다른 사람에게 broadcasting
     */
    @MessageMapping("/signal/{sessionId}")
    public void signaling(
            @DestinationVariable String sessionId,
            SignalingMessage message
    ) {
        System.out.println("[SIGNAL] session=" + sessionId +
                " type=" + message.getType() +
                " sender=" + message.getSenderId());

        // join 메시지라면 사용자 등록
        if ("join".equals(message.getType())) {
            sessionManager.joinSession(sessionId, message.getSenderId());
        }

        // leave 메시지라면 방에서 제거
        if ("leave".equals(message.getType())) {
            sessionManager.leaveSession(sessionId, message.getSenderId());
        }

        // 세션에 있는 모든 사람들에게 메시지 전달 (본인 포함)
        messagingTemplate.convertAndSend("/topic/signal/" + sessionId, message);
    }
}