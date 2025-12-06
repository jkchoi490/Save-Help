package com.save_help.Save_Help.hospital.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@RequiredArgsConstructor
public class HospitalBedSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter((MessageListener) (message, pattern) -> {
            String body = new String(message.getBody());
            messagingTemplate.convertAndSend("/topic/hospital-bed", body);
        });
    }
}
