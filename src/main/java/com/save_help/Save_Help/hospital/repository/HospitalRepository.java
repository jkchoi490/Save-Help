package com.save_help.Save_Help.hospital.repository;

import com.save_help.Save_Help.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 활성화된 병원 전체 조회
    List<Hospital> findByActiveTrue();
}
