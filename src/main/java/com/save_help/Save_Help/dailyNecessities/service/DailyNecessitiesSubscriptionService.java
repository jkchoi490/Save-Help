package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesSubscriptionRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DailyNecessitiesSubscriptionService {

    private final DailyNecessitiesSubscriptionRepository subscriptionRepository;
    private final DailyNecessitiesRepository necessitiesRepository;
    private final UserRepository userRepository;

    public DailyNecessitiesSubscription subscribeNecessity(Long userId, Long necessityId) {
        subscriptionRepository.findByUserIdAndNecessityIdAndActiveTrue(userId, necessityId)
                .ifPresent(subscription -> {
                    throw new IllegalStateException("이미 해당 생필품 알림을 구독 중입니다.");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        DailyNecessities necessity = necessitiesRepository.findById(necessityId)
                .orElseThrow(() -> new IllegalArgumentException("생필품을 찾을 수 없습니다."));

        return subscriptionRepository.save(
                DailyNecessitiesSubscription.builder()
                        .user(user)
                        .necessity(necessity)
                        .category(null)
                        .notifyOnRestock(true)
                        .notifyOnStatusChange(true)
                        .notifyOnPriceChange(false)
                        .active(true)
                        .build()
        );
    }

    public DailyNecessitiesSubscription subscribeCategory(Long userId, DailyNecessitiesCategory category) {
        subscriptionRepository.findByUserIdAndCategoryAndActiveTrue(userId, category)
                .ifPresent(subscription -> {
                    throw new IllegalStateException("이미 해당 카테고리 알림을 구독 중입니다.");
                });

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return subscriptionRepository.save(
                DailyNecessitiesSubscription.builder()
                        .user(user)
                        .category(category)
                        .necessity(null)
                        .notifyOnRestock(true)
                        .notifyOnStatusChange(true)
                        .notifyOnPriceChange(false)
                        .active(true)
                        .build()
        );
    }

    public void unsubscribe(Long subscriptionId) {
        DailyNecessitiesSubscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("구독 정보를 찾을 수 없습니다."));
        subscription.setActive(false);
    }
}