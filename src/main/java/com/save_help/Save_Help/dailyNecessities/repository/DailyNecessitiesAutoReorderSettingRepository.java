package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.AutoReorderSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyNecessitiesAutoReorderSettingRepository  extends JpaRepository<AutoReorderSetting, Long> {

    List<AutoReorderSetting> findByActiveTrueAndNextReorderAtBefore(LocalDateTime now);
}
