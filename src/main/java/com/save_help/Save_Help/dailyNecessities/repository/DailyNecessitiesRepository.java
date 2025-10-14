package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyNecessitiesRepository extends JpaRepository<DailyNecessities, Long> {

    List<DailyNecessities> findByCategory(NecessityCategory category);
    List<DailyNecessities> findByNameContainingIgnoreCase(String name);
    List<DailyNecessities> findByActiveTrue();


    List<DailyNecessities> findByApprovalStatus(DailyNecessities.ApprovalStatus approvalStatus);

    List<DailyNecessities> findByStockLessThanAndApprovalStatus(int threshold, DailyNecessities.ApprovalStatus approvalStatus);

    Arrays findByProvidedBy_Id(Long centerId);

    List<DailyNecessities> findByProvidedBy_IdAndApprovalStatus(Long centerId, DailyNecessities.ApprovalStatus status);

    Optional<DailyNecessities> findByNameAndProvidedBy_Id(String name, Long id);
    //List<DailyNecessities> findByCategoryAndApprovalStatus(DailyNecessities.NecessityCategory category, DailyNecessities.ApprovalStatus status);

}
