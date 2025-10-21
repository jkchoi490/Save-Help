package com.save_help.Save_Help.dailyNecessities.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DailyNecessitiesNotificationService {

    //임시
    public void notifyUser(Long userId, String message) {
        log.info("[USER {} 알림] {}", userId, message);
    }

    public void notifyAdmin(String message) {
        log.warn("[관리자 알림] {}", message);
    }
}