package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface NationalSubsidyRepository extends JpaRepository<NationalSubsidy, Long> {

    // 타입별 검색
    List<NationalSubsidy> findByType(SubsidyType type);

    // 활성화된 보조금만 조회
    List<NationalSubsidy> findByActiveTrue();

    // 이름 키워드 검색
    List<NationalSubsidy> findByNameContainingIgnoreCase(String keyword);

    // 이름 검색
    List<NationalSubsidy> findByNameContaining(String keyword);

    // 현재 신청 가능 보조금
    @Query("SELECT s FROM NationalSubsidy s WHERE s.startDate <= :today AND s.endDate >= :today AND s.active = true")
    List<NationalSubsidy> findAvailableSubsidies(@Param("today")LocalDate today);

    // 세부 필터링
    @Query("""
            SELECT s FROM NationalSubsidy s
            WHERE (:type IS NULL OR s.type = :type)
              AND (:incomeLevel IS NULL OR s.incomeLevel = :incomeLevel)
              AND (:minAge IS NULL OR s.minAge <= :minAge)
              AND (:maxAge IS NULL OR s.maxAge >= :maxAge)
              AND (:disabilityRequired IS NULL OR s.disabilityRequired = :disabilityRequired)
            """)
    List<NationalSubsidy> filter(SubsidyType type, String incomeLevel,
                                 Integer minAge, Integer maxAge, Boolean disabilityRequired);

    @Query("""
        select s
        from NationalSubsidy s
        where s.active = true
          and (s.startDate is null or s.startDate <= :today)
          and (s.endDate is null or s.endDate >= :today)
    """)
    List<NationalSubsidy> findActiveSubsidies(@Param("today") LocalDate today);


    @Query("""
    select s
    from NationalSubsidy s
    where s.active = true
      and (s.startDate is null or s.startDate <= :today)
      and (s.endDate is null or s.endDate >= :today)
      and (s.minAge is null or s.minAge <= :age)
      and (s.maxAge is null or s.maxAge >= :age)
      and (:incomeLevel is null or s.incomeLevel = :incomeLevel)
      and (:disabled = false or s.disabilityRequired = true)
      and (:emergency = false or s.emergencyOnly = true)
""")
    Page<NationalSubsidy> findEligibleSubsidiesForUser(
            @Param("age") int age,
            @Param("incomeLevel") String incomeLevel,
            @Param("disabled") boolean disabled,
            @Param("emergency") boolean emergency,
            @Param("today") LocalDate today,
            Pageable pageable
    );

}

