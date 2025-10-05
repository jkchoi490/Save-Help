package com.save_help.Save_Help.AmbulanceCall.repository;

import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCall;
import com.save_help.Save_Help.AmbulanceCall.entity.AmbulanceCallStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmbulanceCallRepository extends JpaRepository<AmbulanceCall, Long> {

    List<AmbulanceCall> findByStatus(AmbulanceCallStatus status);

    List<AmbulanceCall> findByRequesterId(Long requesterId);

    List<AmbulanceCall> findByEmergencyId(Long emergencyId);
}
