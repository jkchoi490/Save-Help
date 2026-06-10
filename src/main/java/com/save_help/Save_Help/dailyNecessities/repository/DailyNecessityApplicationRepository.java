package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessityApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DailyNecessityApplicationRepository extends JpaRepository<DailyNecessityApplication, Long> {

    boolean existsByUserIdAndSupportIdAndPeriodKey(Long userId, Long supportId, String periodKey);


    List<DailyNecessityApplication> findByUserIdOrderByAppliedAtDesc(Long userId);

    List<DailyNecessityApplication> findBySupportId(Long supportId);

    long countBySupportIdAndStatus(
            Long supportId,
            String status
    );

    List<DailyNecessityApplication> findBySupportIdAndApplyTypeOrderByAppliedAtDesc(Long id, String auto);

    List<DailyNecessityApplication> findByUserIdAndApplyTypeOrderByAppliedAtDesc(
            Long userId,
            String applyType
    );

    List<DailyNecessityApplication> findByCenterIdAndApplyTypeOrderByAppliedAtDesc(
            Long centerId,
            String applyType
    );

    // 자동 신청, 상태별 조회
    List<DailyNecessityApplication> findByApplyTypeAndStatusOrderByAppliedAtDesc(
            String applyType,
            String status
    );

    List<DailyNecessityApplication> findByApplyTypeAndAppliedAtBetweenOrderByAppliedAtDesc(
            String applyType,
            LocalDateTime start,
            LocalDateTime end
    );

    List<DailyNecessityApplication> findByCenterIdAndStatusOrderByAppliedAtDesc(
            Long centerId,
            String status
    );

    long countByApplyTypeAndStatus(
            String applyType,
            String status
    );
}