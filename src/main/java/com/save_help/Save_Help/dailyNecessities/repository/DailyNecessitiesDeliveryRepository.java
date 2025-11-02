package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DailyNecessitiesDeliveryRepository extends JpaRepository<DailyNecessitiesDelivery, Long> {

    @Query("SELECT COUNT(d) FROM DailyNecessitiesDelivery d WHERE d.center.id = :centerId AND d.status = 'IN_TRANSIT'")
    Long countInProgressDeliveries(@Param("centerId") Long centerId);

    @Query("SELECT COUNT(d) FROM DailyNecessitiesDelivery d WHERE d.center.id = :centerId AND d.status = 'DELIVERED'")
    Long countCompletedDeliveries(@Param("centerId") Long centerId);


}
