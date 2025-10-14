package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.StockStatisticsDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DailyNecessitiesStatisticsService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final CommunityCenterRepository centerRepository;

    public DailyNecessitiesStatisticsService(DailyNecessitiesRepository necessitiesRepository,
                                             CommunityCenterRepository centerRepository) {
        this.necessitiesRepository = necessitiesRepository;
        this.centerRepository = centerRepository;
    }

    // 🔹 센터별 재고 합계
    public List<StockStatisticsDto> getStockByCenter() {
        return centerRepository.findAll().stream()
                .map(center -> {
                    Integer total = necessitiesRepository
                            .findByProvidedBy_IdAndApprovalStatus(center.getId(), DailyNecessities.ApprovalStatus.APPROVED)
                            .stream()
                            .mapToInt(DailyNecessities::getStock)
                            .sum();
                    return new StockStatisticsDto(center.getName(), total);
                })
                .toList();
    }

    // 🔹 카테고리별 재고 합계
    /*
    public List<StockStatisticsDto> getStockByCategory() {
        return Arrays.stream(DailyNecessities.NecessityCategory.values())
                .map(category -> {
                    Integer total = necessitiesRepository
                            .findByCategoryAndApprovalStatus(category, DailyNecessities.ApprovalStatus.APPROVED)
                            .stream()
                            .mapToInt(DailyNecessities::getStock)
                            .sum();
                    return new StockStatisticsDto(category.name(), total);
                })
                .toList();
    }

     */
}
