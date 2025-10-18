package com.save_help.Save_Help.messaging.repository;

import com.save_help.Save_Help.messaging.entity.DailyNecessitiesMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesMessageRepository extends JpaRepository<DailyNecessitiesMessage, Long> {
    List<DailyNecessitiesMessage> findBySessionIdOrderBySentAtAsc(Long sessionId);
}