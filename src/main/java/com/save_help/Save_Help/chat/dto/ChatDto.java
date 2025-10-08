package com.save_help.Save_Help.chat.dto;

import com.save_help.Save_Help.chat.entity.ChatType;

import java.time.LocalDateTime;

public class ChatDto {

    public record ChatRequest(
            Long senderId,
            Long receiverId,
            String message,
            ChatType type
    ) {}

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
