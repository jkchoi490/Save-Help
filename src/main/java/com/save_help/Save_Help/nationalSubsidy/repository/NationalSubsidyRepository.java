package com.save_help.Save_Help.nationalSubsidy.repository;

import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidy;
import com.save_help.Save_Help.nationalSubsidy.entity.NationalSubsidyApplication;
import com.save_help.Save_Help.nationalSubsidy.entity.SubsidyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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

    @Query("""
    select s from NationalSubsidy s
    where s.active = true
      and (s.startDate is null or s.startDate <= current_date)
      and (s.endDate is null or s.endDate >= current_date)
""")
    List<NationalSubsidy> findOpenForApplication();


    @Query("""
 select s from NationalSubsidy s
 where s.active = true
   and (s.startDate is null or s.startDate <= current_date)
   and (s.endDate is null or s.endDate >= current_date)
   and (s.emergencyOnly is null or s.emergencyOnly = false or :inEmergency = true)
""")
    List<NationalSubsidy> findOpenForApplicationConsideringEmergency(boolean inEmergency);

    @Query("""
        select s
        from NationalSubsidy s
        where s.isOpen = true
    """)
    List<NationalSubsidy> findOpenSubsidiesForUser(Long userId);

    @Query("""
    select s from NationalSubsidy s
    where s.active = true
      and s.isOpen = true
      and s.startDate <= :today and s.endDate >= :today
      and (s.minAge is null or s.minAge <= :age)
      and (s.maxAge is null or s.maxAge >= :age)
      and (s.incomeLevel is null or s.incomeLevel = :income)
""")
    List<NationalSubsidy> findOpenCandidates(
            @Param("today") LocalDate today,
            @Param("age") int age,
            @Param("income") String income
    );

    @Query("""
        select s from NationalSubsidy s
        where s.active = true and s.isOpen = true
          and (s.startDate is null or s.startDate <= :today)
          and (s.endDate   is null or s.endDate   >= :today)
          and (:age >= coalesce(s.minAge, :age))
          and (:age <= coalesce(s.maxAge, :age))
          and (s.incomeLevel is null or s.incomeLevel = :income)
    """)
    Page<NationalSubsidy> findCandidateForUser(
            @Param("today") LocalDate today,
            @Param("age") int age,
            @Param("income") String income,
            Pageable pageable
    );


    @Query("""
    select (count(s) > 0)
    from NationalSubsidy s
    where s.id = :subsidyId
    and s.active = true
    and s.isOpen = true
    and (s.startDate is null or s.startDate <= :today)
    and (s.endDate   is null or s.endDate   >= :today)
    """)
    boolean existsRunnable(@Param("subsidyId") Long subsidyId,
                           @Param("today") LocalDate today);


    @Query("""
        select s
        from NationalSubsidy s
        where (:type is null or s.type = :type)
        and (:center is null or lower(s.center) like lower(concat('%', :center, '%')))
        and (:from is null or s.startDate >= :from)
        and (:to is null or s.endDate <= :to)
        order by s.id desc
    """)
    Page<NationalSubsidy> searchAdmin(
            @Param("type") SubsidyType type,
            @Param("center") String center,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    @Query("""
        select s
        from NationalSubsidy s
        where s.active = true
          and s.isOpen = true
          and (s.startDate is null or s.startDate <= :today)
          and (s.endDate   is null or s.endDate   >= :today)
    """)
    List<NationalSubsidy> findRunnableSubsidies(@Param("today") LocalDate today);


    @Query("""
    select s
    from NationalSubsidy s
    where s.active = true
      and s.isOpen = true
      and (s.startDate is null or s.startDate <= :today)
      and (s.endDate   is null or s.endDate   >= :today)
""")
    Page<NationalSubsidy> findRunnableSubsidies(@Param("today") LocalDate today, Pageable pageable);

    public interface NationalSubsidyApplicationRepository extends JpaRepository<NationalSubsidyApplication, Long> {

        boolean existsByUser_IdAndSubsidy_Id(Long userId, Long subsidyId);

        @Query("""
        select a.id
        from NationalSubsidyApplication a
        where a.user.id = :userId and a.subsidy.id = :subsidyId
    """)
        Optional<Long> findIdByUserIdAndSubsidyId(@Param("userId") Long userId, @Param("subsidyId") Long subsidyId);
    }

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update NationalSubsidy s set s.isOpen = :open where s.id = :id")
    int updateOpen(@Param("id") Long id, @Param("open") boolean open);


}

