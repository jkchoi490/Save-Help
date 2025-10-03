package com.save_help.Save_Help.chat.controller;

import com.save_help.Save_Help.chat.dto.ChatDto;
import com.save_help.Save_Help.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 메시지 전송
    @PostMapping
    public ResponseEntity<ChatDto.ChatResponse> sendMessage(
            @RequestBody ChatDto.ChatRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(request));
    }

    // 채팅 내역 조회
    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatDto.ChatResponse>> getChatHistory(
            @PathVariable Long senderId,
            @PathVariable Long receiverId) {
        return ResponseEntity.ok(chatService.getChatHistory(senderId, receiverId));
    }

    // 읽음 처리
    @PutMapping("/{chatId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok().build();
    }
}