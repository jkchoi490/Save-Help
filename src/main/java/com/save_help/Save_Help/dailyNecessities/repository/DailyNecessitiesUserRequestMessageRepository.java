package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesUserRequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesUserRequestMessageRepository extends JpaRepository<DailyNecessitiesUserRequestMessage, Long> {

    List<DailyNecessitiesUserRequestMessage> findByCenterIdOrderBySentAtDesc(Long centerId);
    List<DailyNecessitiesUserRequestMessage> findByUserIdOrderBySentAtDesc(Long userId);
}
