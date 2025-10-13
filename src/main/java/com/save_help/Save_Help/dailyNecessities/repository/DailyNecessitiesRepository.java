package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyNecessitiesRepository extends JpaRepository<DailyNecessities, Long> {

    List<DailyNecessities> findByCategory(NecessityCategory category);
    List<DailyNecessities> findByNameContainingIgnoreCase(String name);
    List<DailyNecessities> findByActiveTrue();
    // providedBy(CommunityCenter)의 id로 검색
    List<DailyNecessities> findByProvidedBy_Id(Long centerId);
}
