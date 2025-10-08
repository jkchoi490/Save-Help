package com.save_help.Save_Help.chat.controller;

import com.save_help.Save_Help.chat.dto.ChatDto;
import com.save_help.Save_Help.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
@Tag(name = "Chat API", description = "채팅 API")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    // 메시지 전송
    @Operation(summary = "메시지 전송", description = "새로운 메시지를 전송합니다.")
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatDto.ChatRequest message) {
        // DB 저장
        ChatDto.ChatResponse saved = chatService.sendMessage(message);

        // 수신자에게 실시간 전송
        messagingTemplate.convertAndSend("/queue/" + saved.receiverId(), saved);
    }

    // 채팅 내역 조회
    @Operation(summary = "채팅 내역 조회", description = "채팅 내역을 조회합니다.")
    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatDto.ChatResponse>> getChatHistory(
            @PathVariable Long senderId,
            @PathVariable Long receiverId) {
        return ResponseEntity.ok(chatService.getChatHistory(senderId, receiverId));
    }

    // 특정 유저가 받은 채팅 조회
    @Operation(summary = "수신 채팅 조회", description = "특정 유저가 받은 모든 채팅을 최신순으로 조회합니다.")
    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<ChatDto.ChatResponse>> getReceivedChats(
            @PathVariable Long receiverId) {
        return ResponseEntity.ok(chatService.getReceivedChats(receiverId));
    }

    // 읽음 처리
    @Operation(summary = "읽음 처리", description = "메시지를 읽음 처리합니다")
    @PutMapping("/{chatId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long chatId) {
        chatService.markAsRead(chatId);
        return ResponseEntity.ok().build();
    }
}