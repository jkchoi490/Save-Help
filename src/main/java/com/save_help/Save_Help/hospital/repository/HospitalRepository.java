package com.save_help.Save_Help.hospital.repository;

import com.save_help.Save_Help.hospital.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
