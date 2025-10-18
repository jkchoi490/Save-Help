package com.save_help.Save_Help.messaging.repository;

import com.save_help.Save_Help.messaging.entity.DailyNecessitiesMessageSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyNecessitiesMessageSessionRepository extends JpaRepository<DailyNecessitiesMessageSession, Long> {
    Optional<DailyNecessitiesMessageSession> findByUserPhoneNumber(String userPhoneNumber);
}

