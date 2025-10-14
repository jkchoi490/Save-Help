package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesHistory;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesHistoryRepository;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class DailyNecessitiesStockService {

    private final DailyNecessitiesService necessitiesService;
    private final DailyNecessitiesHistoryRepository historyRepository;
    private final UserRepository userRepository;

    public DailyNecessitiesStockService(DailyNecessitiesService necessitiesService,
                                        DailyNecessitiesHistoryRepository historyRepository,
                                        UserRepository userRepository) {
        this.necessitiesService = necessitiesService;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    // 입고
    public DailyNecessities addStock(Long itemId, Long userId, int quantity) {
        DailyNecessities item = necessitiesService.addStock(itemId, quantity);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        historyRepository.save(new DailyNecessitiesHistory(item, user, quantity, DailyNecessitiesHistory.Type.IN));
        return item;
    }

    // 출고
    public DailyNecessities reduceStock(Long itemId, Long userId, int quantity) {
        DailyNecessities item = necessitiesService.reduceStock(itemId, quantity);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        historyRepository.save(new DailyNecessitiesHistory(item, user, quantity, DailyNecessitiesHistory.Type.OUT));
        return item;
    }

    // 품목별 이력 조회
    public List<DailyNecessitiesHistory> getHistoryByItem(Long itemId) {
        return historyRepository.findByItem_Id(itemId);
    }

    // 센터별 이력 조회
    public List<DailyNecessitiesHistory> getHistoryByCenter(Long centerId) {
        return historyRepository.findByItem_ProvidedBy_Id(centerId);
    }

    // 기간별 이력 조회
    public List<DailyNecessitiesHistory> getHistoryByPeriod(LocalDateTime start, LocalDateTime end) {
        return historyRepository.findByTimestampBetween(start, end);
    }
}
