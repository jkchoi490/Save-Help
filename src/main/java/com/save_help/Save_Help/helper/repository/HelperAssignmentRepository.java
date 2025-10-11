package com.save_help.Save_Help.helper.repository;

import com.save_help.Save_Help.helper.entity.HelperAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelperAssignmentRepository extends JpaRepository<HelperAssignment, Long> {
    List<HelperAssignment> findByHelperId(Long helperId);
    List<HelperAssignment> findByEmergencyId(Long emergencyId);
}