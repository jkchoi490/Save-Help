package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesCenterBoardDto;
import com.save_help.Save_Help.dailyNecessities.dto.StockStatisticsDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDeliveryStatus;
import com.save_help.Save_Help.dailyNecessities.entity.UserNecessityRequest;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesDeliveryRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;

import com.save_help.Save_Help.dailyNecessities.repository.UserNecessityRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DailyNecessitiesStatisticsService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final CommunityCenterRepository centerRepository;
    private final UserNecessityRequestRepository requestRepository;
    private final DailyNecessitiesDeliveryRepository deliveryRepository;


    public DailyNecessitiesStatisticsService(DailyNecessitiesRepository necessitiesRepository,
                                             CommunityCenterRepository centerRepository, UserNecessityRequestRepository requestRepository, DailyNecessitiesDeliveryRepository deliveryRepository) {
        this.necessitiesRepository = necessitiesRepository;
        this.centerRepository = centerRepository;
        this.requestRepository = requestRepository;
        this.deliveryRepository = deliveryRepository;
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

    public DailyNecessitiesCenterBoardDto getCenterBoard(Long centerId) {
        CommunityCenter center = centerRepository.findById(centerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 센터를 찾을 수 없습니다."));

        Long totalStock = necessitiesRepository.findTotalStockByCenter(centerId);
        Long lowStockCount = necessitiesRepository.findLowStockCountByCenter(centerId, 7);

        Long pendingRequests = requestRepository.countByItem_ProvidedBy_IdAndStatus(centerId, UserNecessityRequest.RequestStatus.PENDING);
        Long inProgressDeliveries = deliveryRepository.countByCenter_IdAndStatus(centerId, DailyNecessitiesDeliveryStatus.IN_TRANSIT);
        Long completedDeliveries = deliveryRepository.countByCenter_IdAndStatus(centerId, DailyNecessitiesDeliveryStatus.DELIVERED);

        return DailyNecessitiesCenterBoardDto.builder()
                .centerId(center.getId())
                .centerName(center.getName())
                .totalStock(totalStock != null ? totalStock : 0)
                .lowStockCount(lowStockCount != null ? lowStockCount : 0)
                .pendingRequests(pendingRequests != null ? pendingRequests : 0)
                .inProgressDeliveries(inProgressDeliveries != null ? inProgressDeliveries : 0)
                .completedDeliveries(completedDeliveries != null ? completedDeliveries : 0)
                .build();
    }
}
