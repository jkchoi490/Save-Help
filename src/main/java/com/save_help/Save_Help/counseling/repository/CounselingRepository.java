package com.save_help.Save_Help.counseling.repository;

import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.counseling.entity.CounselingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CounselingRepository extends JpaRepository<Counseling, Long> {
    List<Counseling> findByStatusAndStartAtBetween(CounselingStatus status, LocalDateTime from, LocalDateTime to);
}