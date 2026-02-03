package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyApplication;
import com.save_help.Save_Help.nationalSubsidy.kafka.ApplicationStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubsidyApplicationRepository extends JpaRepository<SubsidyApplication, Long> {
    Optional<SubsidyApplication> findByUserIdAndSubsidyId(Long userId, Long subsidyId);
    List<SubsidyApplication> findByUser_Id(Long userId);
    List<SubsidyApplication> findByStatus(ApplicationStatus status);

}