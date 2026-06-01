package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesContactRequest;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessityApplication;
import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    // 승인 품목 중 이름 검색
    List<DailyNecessities> findByNameContainingIgnoreCaseAndApprovalStatus(
            String keyword,
            DailyNecessities.ApprovalStatus approvalStatus
    );

    List<DailyNecessities> findByProvidedBy_IdAndCategory(Long centerId, DailyNecessitiesCategory category);


    @Query("""
            SELECT COUNT(d)
            FROM DailyNecessities d
            WHERE d.active = true
            """)
    long countActiveItems();

    List<DailyNecessities> findByApprovalStatusAndActiveTrueAndStockGreaterThan(
            DailyNecessities.ApprovalStatus approvalStatus,
            Integer stock
    );

    @Query("""
            SELECT d
            FROM DailyNecessities d
            WHERE d.providedBy.id = :centerId
              AND d.active = true
              AND d.approvalStatus = :approvalStatus
              AND d.stock > 0
              AND (d.expirationDate IS NULL OR d.expirationDate > :now)
            """)
    List<DailyNecessities> findAvailableItemsByCenter(
            @Param("centerId") Long centerId,
            @Param("approvalStatus") DailyNecessities.ApprovalStatus approvalStatus,
            @Param("now") LocalDateTime now
    );

    @Query("""
        select d
        from DailyNecessities d
        where (:centerId is null or d.providedBy.id = :centerId)
          and (:category is null or d.category = :category)
          and (:approvalStatus is null or d.approvalStatus = :approvalStatus)
          and (:active is null or d.active = :active)
          and (
                :keyword is null
                or lower(d.name) like lower(concat('%', :keyword, '%'))
                or lower(d.unit) like lower(concat('%', :keyword, '%'))
                or lower(coalesce(d.supportContents, '')) like lower(concat('%', :keyword, '%'))
              )
    """)
    Page<DailyNecessities> searchItems(@Param("centerId") Long centerId,
                                       @Param("category") DailyNecessitiesCategory category,
                                       @Param("approvalStatus") DailyNecessities.ApprovalStatus approvalStatus,
                                       @Param("active") Boolean active,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
    //임시 주석 처리
    //List<User> findEligibleUsers();

    @Query("""
    select d
    from DailyNecessities d
    where d.active = true
    """)
    List<DailyNecessities> findAllActiveSupports();

    @Query("""
        select d
        from DailyNecessities d
        where d.providedBy.id = :centerId
    """)
    List<DailyNecessities> findByCenterId(@Param("centerId") Long centerId);

    //Repository는 해당 엔티티 기준으로 동작하므로 Repository기능에 맞게 임시 수정
    //List<DailyNecessityApplication> findByUser_Id(Long userId);

    //List<DailyNecessitiesContactRequest> findByDailyNecessities_Id(Long dailyNecessitiesId);

    List<DailyNecessities> findByNameContaining(String keyword);

    // 소득 기준으로 자동 신청 가능한 생필품 조회
    @Query("""
    SELECT d
    FROM DailyNecessities d
    WHERE d.active = true
      AND d.approvalStatus = com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities.ApprovalStatus.APPROVED
      AND d.stock > 0
      AND (:incomeLevel IS NULL OR d.incomeLevel IS NULL OR :incomeLevel <= d.incomeLevel)
""")
    List<DailyNecessities> findEligibleItemsByIncomeLevel(@Param("incomeLevel") Integer incomeLevel);

    List<DailyNecessities> findByProvidedBy_IdAndApprovalStatusAndActiveTrueAndStockGreaterThan(
            Long centerId,
            DailyNecessities.ApprovalStatus approvalStatus,
            Integer stock
    );
}
