package com.save_help.Save_Help.chat.service;

import com.save_help.Save_Help.chat.dto.ChatDto;
import com.save_help.Save_Help.chat.entity.Chat;
import com.save_help.Save_Help.chat.entity.ChatRoom;
import com.save_help.Save_Help.chat.repository.ChatRepository;
import com.save_help.Save_Help.call.entity.Call;
import com.save_help.Save_Help.call.repository.CallRepository;
import com.save_help.Save_Help.chat.repository.ChatRoomRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final CallRepository callRepository;



    public ChatDto.ChatResponse sendMessage(ChatDto.ChatRequest request) {
        User sender = userRepository.findById(request.senderId())
                .orElseThrow(() -> new EntityNotFoundException("발신자 없음"));
        User receiver = userRepository.findById(request.receiverId())
                .orElseThrow(() -> new EntityNotFoundException("수신자 없음"));

        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setMessage(request.message());
        chat.setType(request.type());
        chat.setSentAt(LocalDateTime.now());
        chat.setRead(false);

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

    public List<ChatDto.ChatResponse> getChatHistory(Long userAId, Long userBId) {
        User userA = userRepository.findById(userAId)
                .orElseThrow(() -> new EntityNotFoundException("UserA 없음"));
        User userB = userRepository.findById(userBId)
                .orElseThrow(() -> new EntityNotFoundException("UserB 없음"));

        // userA -> userB 메시지
        List<Chat> chatsAB = chatRepository.findBySenderAndReceiverOrderBySentAtAsc(userA, userB);
        // userB -> userA 메시지
        List<Chat> chatsBA = chatRepository.findBySenderAndReceiverOrderBySentAtAsc(userB, userA);

        // 두 리스트 합치기
        List<Chat> allChats = Stream.concat(chatsAB.stream(), chatsBA.stream())
                .sorted(Comparator.comparing(Chat::getSentAt)) // sentAt 기준 오름차순 정렬
                .collect(Collectors.toList());

        // DTO 변환
        return allChats.stream()
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

    // 특정 유저가 받은 채팅 조회
    public List<ChatDto.ChatResponse> getReceivedChats(Long receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("수신자 없음"));

        List<Chat> chats = chatRepository.findByReceiverOrderBySentAtDesc(receiver);

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

    public void markAsRead(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("채팅 없음"));
        chat.setRead(true);
        chatRepository.save(chat);
    }

}