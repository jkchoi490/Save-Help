package com.save_help.Save_Help.user.repository;

import com.save_help.Save_Help.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);


    @Query("""
        SELECT u FROM User u
        WHERE (:minAge IS NULL OR u.age >= :minAge)
          AND (:maxAge IS NULL OR u.age <= :maxAge)
          AND (:incomeLevel IS NULL OR u.incomeLevel = :incomeLevel)
          AND (:disabilityRequired = false OR u.disabled = true)
          AND (:emergencyOnly = false OR u.inEmergency = true)
    """)
    Page<User> findEligibleUsersForSubsidy(
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("incomeLevel") String incomeLevel,
            @Param("disabilityRequired") boolean disabilityRequired,
            @Param("emergencyOnly") boolean emergencyOnly,
            Pageable pageable
    );

    @Query("""
    select u
    from User u
    where not exists (
    select 1
    from SubsidyApplication a
    where a.user = u
    and a.subsidy.id = :subsidyId
    )
    """)
    List<User> findEligibleUsersForSubsidy(@Param("subsidyId") Long subsidyId);

}
