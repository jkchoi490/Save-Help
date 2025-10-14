package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperAssignmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HelperAssignmentHistoryRepository extends JpaRepository<HelperAssignmentHistory, Long> {
    List<HelperAssignmentHistory> findByHelperId(Long helperId);
    List<HelperAssignmentHistory> findByEmergencyId(Long emergencyId);


    @Query("SELECT COUNT(h) FROM HelperAssignmentHistory h WHERE h.helperId = :helperId AND h.action = 'ASSIGNED'")
    Long countAssignedByHelper(@Param("helperId") Long helperId);


    @Query("SELECT MAX(h.actionAt) FROM HelperAssignmentHistory h WHERE h.helperId = :helperId")
    LocalDateTime findLastAssignedAt(@Param("helperId") Long helperId);
}