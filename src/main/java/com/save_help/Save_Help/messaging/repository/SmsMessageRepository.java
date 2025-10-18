package com.save_help.Save_Help.messaging.repository;

import com.save_help.Save_Help.messaging.entity.SmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsMessageRepository extends JpaRepository<SmsMessage, Long> {}