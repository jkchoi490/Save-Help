package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyNecessitiesNotificationRepository
        extends JpaRepository<DailyNecessitiesNotification, Long> {
}