package com.save_help.Save_Help.hospital.repository;

import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.hospital.entity.HospitalType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    // 활성화된 병원 전체 조회
    List<Hospital> findByActiveTrue();

    // HospitalType과 active=true 조건으로 조회
    List<Hospital> findByTypeAndActiveTrue(HospitalType type);

    // 필요 시 남은 병상 > 0 조건도 추가 가능
    List<Hospital> findByTypeAndActiveTrueAndBedCountGreaterThan(HospitalType type, int bedCount);
}
