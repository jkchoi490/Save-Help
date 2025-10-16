package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCenterMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesCenterMessageRepository extends JpaRepository<DailyNecessitiesCenterMessage, Long> {
    List<DailyNecessitiesCenterMessage> findByUserIdOrderBySentAtDesc(Long userId);
}