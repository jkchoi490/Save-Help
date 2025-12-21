package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesRequestRepository extends JpaRepository<DailyNecessitiesRequest, Long> {

    List<DailyNecessitiesRequest> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<DailyNecessitiesRequest> findByCenterIdOrderByCreatedAtDesc(Long centerId);

    List<DailyNecessitiesRequest> findByCenterIdAndStatusOrderByCreatedAtDesc(Long centerId, DailyNecessitiesRequest.Status status);

    List<DailyNecessitiesRequest> findByStatusOrderByCreatedAtDesc(DailyNecessitiesRequest.Status status);
}