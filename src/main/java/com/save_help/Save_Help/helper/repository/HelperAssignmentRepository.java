package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.AssignmentProgressStatus;
import com.save_help.Save_Help.helper.entity.HelperAssignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelperAssignmentRepository extends JpaRepository<HelperAssignment, Long> {
    List<HelperAssignment> findByHelperId(Long helperId);
    Optional<HelperAssignment> findByEmergencyId(Long emergencyId);

    Page<HelperAssignment> findByHelperIdOrderByAssignedAtDesc(Long helperId, Pageable pageable);

    Page<HelperAssignment> findByHelperIdAndProgressStatusOrderByAssignedAtDesc(
            Long helperId, AssignmentProgressStatus progressStatus, Pageable pageable
    );

    Optional<HelperAssignment> findByIdAndHelperId(Long id, Long helperId);
}