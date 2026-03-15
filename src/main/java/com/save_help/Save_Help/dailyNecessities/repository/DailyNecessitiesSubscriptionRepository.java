package com.save_help.Save_Help.dailyNecessities.repository;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesCategory;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyNecessitiesSubscriptionRepository extends JpaRepository<DailyNecessitiesSubscription, Long> {

    List<DailyNecessitiesSubscription> findByNecessityAndActiveTrue(DailyNecessities necessity);

    List<DailyNecessitiesSubscription> findByCategoryAndActiveTrue(DailyNecessitiesCategory category);

    List<DailyNecessitiesSubscription> findByUserIdAndActiveTrue(Long userId);

    Optional<DailyNecessitiesSubscription> findByUserIdAndNecessityIdAndActiveTrue(Long userId, Long necessityId);

    Optional<DailyNecessitiesSubscription> findByUserIdAndCategoryAndActiveTrue(Long userId, DailyNecessitiesCategory category);
}