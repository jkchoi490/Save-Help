package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface NationalSubsidyApplicationRepository extends JpaRepository<NationalSubsidyApplication, Long> {

    @Query("""
        SELECT s FROM NationalSubsidy s
        WHERE s.active = true
          AND (s.startDate IS NULL OR s.startDate <= :today)
          AND (s.endDate IS NULL OR s.endDate >= :today)

          AND (:age IS NULL OR (
                (s.minAge IS NULL OR s.minAge <= :age)
            AND (s.maxAge IS NULL OR s.maxAge >= :age)
          ))

          AND (:incomeLevel IS NULL OR s.incomeLevel IS NULL OR s.incomeLevel = :incomeLevel)

          AND (:disabled = false OR s.disabilityRequired IS NULL OR s.disabilityRequired = true)
          AND (:inEmergency = false OR s.emergencyOnly IS NULL OR s.emergencyOnly = true)
    """)
    Page<NationalSubsidy> findEligibleSubsidiesForUser(
            @Param("age") Integer age,
            @Param("incomeLevel") String incomeLevel,
            @Param("disabled") boolean disabled,
            @Param("inEmergency") boolean inEmergency,
            @Param("today") LocalDate today,
            Pageable pageable
    );

    @Query("""
   SELECT a.id FROM NationalSubsidyApplication a
   JOIN a.subsidy s
   WHERE a.active = true
     AND s.active = true
     AND (s.startDate IS NULL OR s.startDate <= :today)
     AND (s.endDate IS NULL OR s.endDate >= :today)
""")
    Page<Long> findNationalSubsidyApplicationIds(LocalDate today, Pageable pageable);

    boolean existsByUser_IdAndSubsidy_IdAndActiveTrue(Long userId, Long subsidyId);

    Page<NationalSubsidyApplication> findByStatus(NationalSubsidyApplication.Status status, Pageable pageable);


    Page<NationalSubsidyApplication> findByUser_IdAndAppliedByOrderByCreatedAtDesc(
                Long userId,
                NationalSubsidyApplication.AppliedBy appliedBy,
                Pageable pageable
        );


    long countBySubsidy_Id(Long subsidyId);

    Page<NationalSubsidyApplication> findBySubsidy_IdOrderByCreatedAtDesc(Long subsidyId, Pageable pageable);

    @Query("""
    select a
    from NationalSubsidyApplication a
    where a.createdAt between :from and :to
""")
    Page<NationalSubsidyApplication> findCreatedBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            Pageable pageable
    );

    // 중복 신청 검사를 위한 메서드
    boolean existsByUser_IdAndSubsidy_Id(
            Long userId,
            Long subsidyId
    );

    // 중복 신청 검사를 위한 메서드
    boolean existsByEventId(String eventId);

    // 중복 신청 검사를 위한 메서드
    Optional<NationalSubsidyApplication>
    findByUser_IdAndSubsidy_Id(
            Long userId,
            Long subsidyId
    );
}
