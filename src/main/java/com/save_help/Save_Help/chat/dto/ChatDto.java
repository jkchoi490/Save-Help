package com.save_help.Save_Help.chat.dto;

import com.save_help.Save_Help.chat.entity.ChatType;

import java.time.LocalDateTime;

public class ChatDto {

    // 메시지 전송 요청 DTO
    public record ChatRequest(
            Long senderId,
            Long receiverId,
            String message,
            ChatType type,
            Long callId
    ) {}

    // 메시지 응답 DTO
    public record ChatResponse(
            Long id,
            Long senderId,
            Long receiverId,
            String message,
            ChatType type,
            LocalDateTime sentAt,
            boolean read
    ) {}
}
