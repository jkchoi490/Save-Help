package com.save_help.Save_Help.websocket.config;

import com.save_help.Save_Help.hospital.websocket.HospitalBedSubscriber;
import com.save_help.Save_Help.hospital.websocket.HospitalWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class HospitalWebSocketConfig implements WebSocketConfigurer {

    private final HospitalBedSubscriber bedSubscriber;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new HospitalWebSocketHandler(bedSubscriber), "/ws/hospital-beds")
                .setAllowedOrigins("*")
                .withSockJS();
    }
}
