package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesReview;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DailyNecessitiesReviewRepository extends JpaRepository<DailyNecessitiesReview, Long> {
    List<DailyNecessitiesReview> findByItem(DailyNecessities item);
}
