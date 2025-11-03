package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperNotificationDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperNotification;
import com.save_help.Save_Help.helper.repository.HelperNotificationHistoryRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelperNotificationHistoryService {

    private final HelperNotificationHistoryRepository historyRepository;
    private final HelperRepository helperRepository;

    // 알림 내역 저장
    @Transactional
    public void logNotification(Long helperId, String message, boolean success, String errorReason) {

        Helper helper = helperRepository.findById(helperId)
                .orElseThrow(() -> new IllegalArgumentException("헬퍼를 찾을 수 없습니다."));

        HelperNotification history = HelperNotification.builder()
                .helper(helper)
                .message(message)
                .success(success)
                .errorReason(errorReason)
                .build();

        historyRepository.save(history);
    }

    // Helper의 알림 이력 조회
    @Transactional(readOnly = true)
    public List<HelperNotificationDto> getNotificationHistory(Long helperId) {
        List<HelperNotification> list = historyRepository.findByHelper_IdOrderBySentAtDesc(helperId);
        return list.stream()
                .map(h -> HelperNotificationDto.builder()
                        .id(h.getId())
                        .helperId(helperId)
                        .helperName(h.getHelper().getName())
                        .message(h.getMessage())
                        .sentAt(h.getSentAt())
                        .success(h.isSuccess())
                        .errorReason(h.getErrorReason())
                        .build())
                .collect(Collectors.toList());
    }
}
