package com.save_help.Save_Help.chat.controller;

import com.save_help.Save_Help.chat.dto.ChatDto;
import com.save_help.Save_Help.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;

    // 메시지 전송
    @Operation(summary = "메시지 전송", description = "새로운 메시지를 전송합니다.")
    @PostMapping
    public ResponseEntity<ChatDto.ChatResponse> sendMessage(
            @RequestBody ChatDto.ChatRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(request));
    }

    // 채팅 내역 조회
    @Operation(summary = "채팅 내역 조회", description = "채팅 내역을 조회합니다.")
    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatDto.ChatResponse>> getChatHistory(
            @PathVariable Long senderId,
            @PathVariable Long receiverId) {
        return ResponseEntity.ok(chatService.getChatHistory(senderId, receiverId));
    }

    // 읽음 처리
    @Operation(summary = "읽음 처리", description = "메시지를 읽음 처리합니다")
    @PutMapping("/{chatId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok().build();
    }
}