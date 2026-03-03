package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyNecessitiesRepository extends JpaRepository<DailyNecessities, Long> , JpaSpecificationExecutor<DailyNecessities> {

    List<DailyNecessities> findByCategory(DailyNecessitiesCategory category);
    List<DailyNecessities> findByNameContainingIgnoreCase(String name);
    List<DailyNecessities> findByActiveTrue();
    List<DailyNecessities> findByApprovalStatus(DailyNecessities.ApprovalStatus approvalStatus);
    List<DailyNecessities> findByStockLessThanAndApprovalStatus(int threshold, DailyNecessities.ApprovalStatus approvalStatus);

    List<DailyNecessities> findByProvidedBy_Id(Long centerId);
    List<DailyNecessities> findByProvidedBy_IdAndApprovalStatus(Long centerId, DailyNecessities.ApprovalStatus status);
    Optional<DailyNecessities> findByNameAndProvidedBy_Id(String name, Long id);
    List<DailyNecessities> findByCategoryInAndStockGreaterThan(List<DailyNecessitiesCategory> topCategories, int stock);
    List<DailyNecessities> findTop10ByOrderByRequestCountDesc();


    @Query("SELECT SUM(d.stock) FROM DailyNecessities d WHERE d.providedBy.id = :centerId")
    Long findTotalStockByCenter(@Param("centerId") Long centerId);

    @Query("SELECT COUNT(d) FROM DailyNecessities d WHERE d.providedBy.id = :centerId AND d.stock < :threshold")
    Long findLowStockCountByCenter(@Param("centerId") Long centerId, @Param("threshold") int threshold);

    List<DailyNecessities> findByStockLessThan(int threshold);
}
