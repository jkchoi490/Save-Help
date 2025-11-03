package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesAlertLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyNecessitiesAlertLogRepository extends JpaRepository<DailyNecessitiesAlertLog, Long> {
}
