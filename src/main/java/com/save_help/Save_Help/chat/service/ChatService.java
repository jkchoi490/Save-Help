package com.save_help.Save_Help.chat.service;

import com.save_help.Save_Help.chat.dto.ChatDto;
import com.save_help.Save_Help.chat.entity.Chat;
import com.save_help.Save_Help.chat.repository.ChatRepository;
import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.call.repository.CallRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final CallRepository callRepository;

    // 메시지 저장
    public ChatDto.ChatResponse sendMessage(ChatDto.ChatRequest request) {
        User sender = userRepository.findById(request.senderId())
                .orElseThrow(() -> new EntityNotFoundException("발신자 없음"));
        User receiver = userRepository.findById(request.receiverId())
                .orElseThrow(() -> new EntityNotFoundException("수신자 없음"));
        Call call = request.callId() != null
                ? callRepository.findById(request.callId()).orElse(null)
                : null;

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setMessage(request.message());
        chat.setType(request.type());
        chat.setSentAt(LocalDateTime.now());
        chat.setRead(false);
        chat.setCall(call);

        Chat saved = chatRepository.save(chat);

        return new ChatDto.ChatResponse(
                saved.getId(),
                saved.getSender().getId(),
                saved.getReceiver().getId(),
                saved.getMessage(),
                saved.getType(),
                saved.getSentAt(),
                saved.isRead()
        );
    }

    // 두 유저 간의 채팅 내역 조회
    public List<ChatDto.ChatResponse> getChatHistory(Long senderId, Long receiverId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("발신자 없음"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("수신자 없음"));

        List<Chat> chats = chatRepository.findBySenderAndReceiverOrderBySentAtAsc(sender, receiver);

        return chats.stream()
                .map(chat -> new ChatDto.ChatResponse(
                        chat.getId(),
                        chat.getSender().getId(),
                        chat.getReceiver().getId(),
                        chat.getMessage(),
                        chat.getType(),
                        chat.getSentAt(),
                        chat.isRead()
                ))
                .collect(Collectors.toList());
    }

    // 읽음 처리
    public void markAsRead(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.setRead(true);
        chatRepository.save(chat);
    }
}