package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    List<DailyNecessities> findByProvidedBy_Id(Long centerId);

    List<DailyNecessities> findByProvidedBy_IdAndApprovalStatus(Long centerId, DailyNecessities.ApprovalStatus status);

    Optional<DailyNecessities> findByNameAndProvidedBy_Id(String name, Long id);

    List<DailyNecessities> findByCategoryInAndStockGreaterThan(List<NecessityCategory> topCategories, int stock);

    List<DailyNecessities> findTop10ByOrderByRequestCountDesc();

    // 특정 카테고리 또는 이름으로 활성 + 승인된 재고 검색
    @Query("SELECT d FROM DailyNecessities d WHERE d.active = true AND d.approvalStatus = 'APPROVED' " +
            "AND (d.name = :name OR d.category = :category) AND d.stock > 0")
    List<DailyNecessities> findAvailableByNameOrCategory(String name, NecessityCategory category);

    //List<DailyNecessities> findByCategoryAndApprovalStatus(DailyNecessities.NecessityCategory category, DailyNecessities.ApprovalStatus status);




    // providedBy(CommunityCenter)의 id로 검색

}
