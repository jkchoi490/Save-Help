package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyNecessitiesRestockForecastService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final DailyNecessitiesHistoryRepository historyRepository;
    private final DailyNecessitiesRestockSuggestionRepository suggestionRepository;

    @Scheduled(cron = "0 0 7 * * *")
    public void generateRestockSuggestions() {
        log.info("자동 재고 예측 및 발주 제안 생성 시작...");

        List<DailyNecessities> allItems = necessitiesRepository.findAll();
        for (DailyNecessities item : allItems) {
            int avgDailyUsage = historyRepository.getAverageDailyUsage(item.getId(), 7); // 최근 7일 평균 사용량
            if (avgDailyUsage <= 0) continue;

            int currentStock = item.getStock();
            int daysLeft = currentStock / avgDailyUsage;

            if (daysLeft <= 3) { // 3일 이하 남으면 발주 제안
                DailyNecessitiesRestockSuggestion suggestion = DailyNecessitiesRestockSuggestion.builder()
                        .necessity(item)
                        .predictedOutOfStockDate(LocalDateTime.now().plusDays(daysLeft))
                        .createdAt(LocalDateTime.now())
                        .status(DailyNecessitiesRestockSuggestion.SuggestionStatus.PENDING)
                        .build();

                suggestionRepository.save(suggestion);
                log.info("발주 제안 생성 - {} (예상 소진일: {})", item.getName(), suggestion.getPredictedOutOfStockDate());
            }
        }
    }

    public List<DailyNecessitiesRestockSuggestion> getPendingSuggestions() {
        return suggestionRepository.findByStatus(DailyNecessitiesRestockSuggestion.SuggestionStatus.PENDING);
    }

    public void approveSuggestion(Long id) {
        suggestionRepository.findById(id).ifPresent(s -> {
            s.setStatus(DailyNecessitiesRestockSuggestion.SuggestionStatus.APPROVED);
            suggestionRepository.save(s);
        });
    }

    public void rejectSuggestion(Long id) {
        suggestionRepository.findById(id).ifPresent(s -> {
            s.setStatus(DailyNecessitiesRestockSuggestion.SuggestionStatus.REJECTED);
            suggestionRepository.save(s);
        });
    }
}
