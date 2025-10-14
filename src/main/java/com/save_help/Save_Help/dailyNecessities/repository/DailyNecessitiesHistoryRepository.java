package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyNecessitiesHistoryRepository extends JpaRepository<DailyNecessitiesHistory, Long> {

    List<DailyNecessitiesHistory> findByItem_Id(Long itemId);

    List<DailyNecessitiesHistory> findByItem_ProvidedBy_Id(Long centerId);

    List<DailyNecessitiesHistory> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
