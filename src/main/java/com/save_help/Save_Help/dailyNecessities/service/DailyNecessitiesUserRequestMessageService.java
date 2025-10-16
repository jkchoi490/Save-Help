package com.save_help.Save_Help.dailyNecessities.service;


import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesUserRequestMessageDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesUserRequestMessage;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesUserRequestMessageRepository;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesUserRequestMessageService {

    private final DailyNecessitiesUserRequestMessageRepository repository;
    private final UserRepository userRepository;
    private final CommunityCenterRepository centerRepository;

    // 사용자 요청 생성
    public DailyNecessitiesUserRequestMessageDto create(Long userId, Long centerId, String message) {
        var user = userRepository.findById(userId).orElseThrow();
        var center = centerRepository.findById(centerId).orElseThrow();

        DailyNecessitiesUserRequestMessage request = DailyNecessitiesUserRequestMessage.builder()
                .user(user)
                .center(center)
                .message(message)
                .build();

        return DailyNecessitiesUserRequestMessageDto.fromEntity(repository.save(request));
    }

    // 특정 센터가 받은 요청 조회
    public List<DailyNecessitiesUserRequestMessageDto> getRequestsForCenter(Long centerId) {
        return repository.findByCenterIdOrderBySentAtDesc(centerId)
                .stream()
                .map(DailyNecessitiesUserRequestMessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 보낸 요청 조회
    public List<DailyNecessitiesUserRequestMessageDto> getRequestsByUser(Long userId) {
        return repository.findByUserIdOrderBySentAtDesc(userId)
                .stream()
                .map(DailyNecessitiesUserRequestMessageDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 요청 처리 상태 변경
    public void markAsProcessed(Long requestId) {
        var request = repository.findById(requestId).orElseThrow();
        request.setProcessed(true);
        repository.save(request);
    }
}