package com.save_help.Save_Help.hospital.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class HospitalWebSocketHandler extends TextWebSocketHandler {

    private final HospitalBedSubscriber subscriber;

    public HospitalWebSocketHandler(HospitalBedSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        subscriber.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        subscriber.removeSession(session);
    }
}