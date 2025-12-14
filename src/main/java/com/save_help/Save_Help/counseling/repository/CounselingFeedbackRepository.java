package com.save_help.Save_Help.counseling.repository;

import com.save_help.Save_Help.counseling.entity.CounselingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounselingFeedbackRepository extends JpaRepository<CounselingFeedback, Long> {
    Optional<CounselingFeedback> findByCounselingId(Long counselingId);
}