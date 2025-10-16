package com.save_help.Save_Help.dailyNecessities.service;


import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesCenterMessageDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCenterMessage;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesCenterMessageRepository;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesCenterMessageService {

    private final DailyNecessitiesCenterMessageRepository messageRepository;
    private final CommunityCenterRepository centerRepository;
    private final UserRepository userRepository;

    // 메시지 전송
    public DailyNecessitiesCenterMessageDto sendMessage(Long centerId, Long userId, String message) {
        var center = centerRepository.findById(centerId)
                .orElseThrow(() -> new IllegalArgumentException("센터가 존재하지 않습니다."));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        DailyNecessitiesCenterMessage msg = DailyNecessitiesCenterMessage.builder()
                .center(center)
                .user(user)
                .message(message)
                .build();

        return DailyNecessitiesCenterMessageDto.fromEntity(messageRepository.save(msg));
    }

    // 사용자 메시지 조회
    public List<DailyNecessitiesCenterMessageDto> getMessagesForUser(Long userId) {
        return messageRepository.findByUserIdOrderBySentAtDesc(userId)
                .stream()
                .map(DailyNecessitiesCenterMessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 메시지 읽음 처리
    public void markAsRead(Long messageId) {
        var msg = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("메시지가 존재하지 않습니다."));
        msg.setRead(true);
        messageRepository.save(msg);
    }
}
