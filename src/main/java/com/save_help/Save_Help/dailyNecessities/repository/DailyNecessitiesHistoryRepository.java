package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyNecessitiesHistoryRepository extends JpaRepository<DailyNecessitiesHistory, Long> {

    List<DailyNecessitiesHistory> findByItem_Id(Long itemId);

    List<DailyNecessitiesHistory> findByItem_ProvidedBy_Id(Long centerId);

    List<DailyNecessitiesHistory> findByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COALESCE(AVG(h.quantity), 0) FROM DailyNecessitiesHistory h " +
            "WHERE h.item.id = :itemId AND h.type = 'OUT' " +
            "AND h.timestamp >= :startDate")
    Double getAverageDailyUsage(@Param("itemId") Long itemId,
                                @Param("startDate") LocalDateTime startDate);

}
