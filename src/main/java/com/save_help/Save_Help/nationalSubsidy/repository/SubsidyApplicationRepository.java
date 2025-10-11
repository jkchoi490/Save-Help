package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubsidyApplicationRepository extends JpaRepository<SubsidyApplication, Long> {
    Optional<SubsidyApplication> findByUserIdAndSubsidyId(Long userId, Long subsidyId);
}