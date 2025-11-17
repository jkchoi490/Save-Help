package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesNotification;
import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesNotificationRepository;
import com.save_help.Save_Help.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DailyNecessitiesNotificationService {

    private final DailyNecessitiesNotificationRepository notificationRepository;

    public DailyNecessitiesNotificationService(DailyNecessitiesNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void sendRequestStatusNotification(User user, DailyNecessities item,
                                              UserNecessityRequest.RequestStatus status) {

        String message;

        if (status == UserNecessityRequest.RequestStatus.APPROVED) {
            message = String.format("'%s' 생필품 신청이 승인되었습니다.", item.getName());
        } else {
            message = String.format("'%s' 생필품 신청이 보류되었습니다.", item.getName());
        }

        DailyNecessitiesNotification notification = new DailyNecessitiesNotification();
        notification.setUser(user);
        notification.setMessage(message);

        notification.setType(
                status == UserNecessityRequest.RequestStatus.APPROVED ?
                        DailyNecessitiesNotification.NotificationType.REQUEST_APPROVED :
                        DailyNecessitiesNotification.NotificationType.REQUEST_REJECTED
        );

        notificationRepository.save(notification);
    }
    //임시
    public void notifyUser(Long userId, String message) {
        log.info("[USER {} 알림] {}", userId, message);
    }

    public void notifyAdmin(String message) {
        log.warn("[관리자 알림] {}", message);
    }
}