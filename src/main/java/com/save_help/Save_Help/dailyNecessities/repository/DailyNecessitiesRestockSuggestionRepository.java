package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesRestockSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyNecessitiesRestockSuggestionRepository extends JpaRepository<DailyNecessitiesRestockSuggestion, Long> {
    List<DailyNecessitiesRestockSuggestion> findByStatus(DailyNecessitiesRestockSuggestion.SuggestionStatus status);
}
