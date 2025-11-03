package com.save_help.Save_Help.helper.repository;


import com.save_help.Save_Help.helper.entity.HelperNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperNotificationHistoryRepository extends JpaRepository<HelperNotification, Long> {

    List<HelperNotification> findByHelper_IdOrderBySentAtDesc(Long helperId);
}
