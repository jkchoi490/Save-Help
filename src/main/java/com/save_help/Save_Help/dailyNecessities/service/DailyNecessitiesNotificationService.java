package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesNotificationDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesNotification;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesNotificationRepository;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.DailyNecessitiesAutoApplyCompletedEvent;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class DailyNecessitiesNotificationService {

    private final DailyNecessitiesNotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public DailyNecessitiesNotificationService(DailyNecessitiesNotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    //임시
    public void notifyUser(Long userId, String message) {
        log.info("[USER {} 알림] {}", userId, message);
    }

    public void notifyAdmin(String message) {
        log.warn("[관리자 알림] {}", message);
    }

    public DailyNecessitiesNotificationDto saveAutoApplyCompletedNotification(
            DailyNecessitiesAutoApplyCompletedEvent event
    ) {
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        DailyNecessitiesNotification notification = new DailyNecessitiesNotification();
        notification.setUser(user);
        notification.setTitle("생필품 자동 신청 완료");
        notification.setContent(
                String.format("'%s' 품목이 %d개 자동 신청되었습니다.", event.getItemName(), event.getQuantity())
        );
        notification.setType(DailyNecessitiesNotification.NotificationType.AUTO_APPLY_COMPLETED);

        return DailyNecessitiesNotificationDto.fromEntity(notificationRepository.save(notification));
    }

    public DailyNecessitiesNotificationDto saveAutoApplyFailedNotification(
            DailyNecessitiesAutoApplyFailedEvent event
    ) {
        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        DailyNecessitiesNotification notification = new DailyNecessitiesNotification();
        notification.setUser(user);
        notification.setTitle("생필품 자동 신청 실패");
        notification.setContent(
                String.format("'%s' 자동 신청에 실패했습니다. 사유: %s", event.getItemName(), event.getReason())
        );
        notification.setType(DailyNecessitiesNotification.NotificationType.AUTO_APPLY_FAILED);

        return DailyNecessitiesNotificationDto.fromEntity(notificationRepository.save(notification));
    }

    @Transactional(readOnly = true)
    public List<DailyNecessitiesNotificationDto> getNotifications(Long userId) {
        return notificationRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(DailyNecessitiesNotificationDto::fromEntity)
                .toList();
    }

    public void markAsRead(Long notificationId) {
        DailyNecessitiesNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notification.setRead(true);
    }

    @Transactional(readOnly = true)
    public long countUnread(Long userId) {
        return notificationRepository.countByUser_IdAndIsReadFalse(userId);
    }
}