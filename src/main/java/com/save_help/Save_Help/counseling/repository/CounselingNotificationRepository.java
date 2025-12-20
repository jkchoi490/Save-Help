package com.save_help.Save_Help.counseling.repository;

import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.counseling.entity.CounselingNotification;
import com.save_help.Save_Help.counseling.entity.RecipientType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselingNotificationRepository  extends JpaRepository<CounselingNotification, Long> {
    Page<CounselingNotification> findByRecipientTypeAndRecipientIdOrderByCreatedAtDesc(
            RecipientType recipientType, Long recipientId, Pageable pageable
    );

    boolean existsByUniqueKey(String uniqueKey);
}