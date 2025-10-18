package com.save_help.Save_Help.messaging.repository;

import com.save_help.Save_Help.messaging.entity.SmsSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsSessionRepository extends JpaRepository<SmsSession, Long> {
    Optional<SmsSession> findByUserPhoneAndActiveTrue(String userPhone);
}