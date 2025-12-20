package com.save_help.Save_Help.nationalSubsidy.repository;


import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationalSubsidyNotificationRepository extends JpaRepository<NationalSubsidyNotification, Long> {
    Page<NationalSubsidyNotification> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}