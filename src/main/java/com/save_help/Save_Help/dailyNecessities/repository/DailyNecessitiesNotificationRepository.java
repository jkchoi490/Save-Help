package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesNotificationRepository
        extends JpaRepository<DailyNecessitiesNotification, Long> {

    List<DailyNecessitiesNotification> findByUser_IdOrderByCreatedAtDesc(Long userId);

    long countByUser_IdAndIsReadFalse(Long userId);
}