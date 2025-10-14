package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.dailyNecessities.dto.DailyNecessitiesDto;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.NecessityCategory;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DailyNecessitiesService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final CommunityCenterRepository centerRepository;

    public DailyNecessitiesService(DailyNecessitiesRepository necessitiesRepository,
                                   CommunityCenterRepository centerRepository) {
        this.necessitiesRepository = necessitiesRepository;
        this.centerRepository = centerRepository;
    }

    // 생성
    public DailyNecessitiesDto create(DailyNecessitiesDto dto) {
        CommunityCenter center = centerRepository.findById(dto.getCenterId())
                .orElseThrow(() -> new EntityNotFoundException("CommunityCenter not found"));

        DailyNecessities item = new DailyNecessities(
                dto.getName(),
                dto.getCategory(),
                dto.getUnit(),
                dto.getStock(),
                dto.getExpirationDate(),
                center
        );

        DailyNecessities saved = necessitiesRepository.save(item);
        return DailyNecessitiesDto.fromEntity(saved);
    }

    // 전체 조회
    public List<DailyNecessitiesDto> getAll() {
        return necessitiesRepository.findByActiveTrue()
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 카테고리별 조회
    public List<DailyNecessitiesDto> getByCategory(NecessityCategory category) {
        return necessitiesRepository.findByCategory(category)
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 이름 검색
    public List<DailyNecessitiesDto> searchByName(String name) {
        return necessitiesRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(DailyNecessitiesDto::fromEntity)
                .toList();
    }

    // 수정
    public DailyNecessitiesDto update(Long id, DailyNecessitiesDto dto) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setUnit(dto.getUnit());
        item.setStock(dto.getStock());
        item.setExpirationDate(dto.getExpirationDate());

        DailyNecessities updated = necessitiesRepository.save(item);
        return DailyNecessitiesDto.fromEntity(updated);
    }

    // 삭제(비활성화)
    public void delete(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.deactivate();
        necessitiesRepository.save(item);
    }

    // 단건 조회
    public DailyNecessitiesDto getById(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("생필품을 찾을 수 없습니다."));
        return DailyNecessitiesDto.fromEntity(item);
    }

    //관리자 승인
    public DailyNecessities approveItem(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.approve();
        return necessitiesRepository.save(item);
    }

    // 관리자 거부
    public DailyNecessities rejectItem(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.reject();
        return necessitiesRepository.save(item);
    }

    // 관리자 전체 조회 (모든 승인 상태 포함)
    public List<DailyNecessities> getAllForAdmin() {
        return necessitiesRepository.findAll();
    }

    // 사용자 조회 시 승인된 품목만 반환
    public List<DailyNecessities> getAllForUser() {
        return necessitiesRepository.findByApprovalStatus(DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 재고 조회 (센터별)
    /*
    public List<DailyNecessities> getStockByCenter(Long centerId) {

        return necessitiesRepository.findByProvidedBy_Id(centerId)
                .stream()
                .filter(item -> item.getApprovalStatus() == DailyNecessities.ApprovalStatus.APPROVED)
                .toList();

    } */
    // 재고 조회 (전체)
    public List<DailyNecessities> getAllStock() {
        return necessitiesRepository.findByApprovalStatus(DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 재고 입고
    public DailyNecessities addStock(Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("입고 수량은 1 이상이어야 합니다.");
        DailyNecessities item = necessitiesRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));
        item.setStock(item.getStock() + quantity);
        return necessitiesRepository.save(item);
    }

    // 재고 출고
    public DailyNecessities reduceStock(Long itemId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("출고 수량은 1 이상이어야 합니다.");
        DailyNecessities item = necessitiesRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다."));

        if (item.getStock() < quantity) {
            throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + item.getStock());
        }

        item.setStock(item.getStock() - quantity);
        return necessitiesRepository.save(item);
    }

    // 재고 부족 품목 조회 (임계치 기준)
    public List<DailyNecessities> getLowStockItems(int threshold) {
        return necessitiesRepository.findByStockLessThanAndApprovalStatus(threshold, DailyNecessities.ApprovalStatus.APPROVED);
    }

    // 기부 승인 시 기존 품목이 있으면 반환, 없으면 생성
    public DailyNecessities findOrCreateItem(String name, NecessityCategory category, String unit, CommunityCenter center) {
        return necessitiesRepository.findByNameAndProvidedBy_Id(name, center.getId())
                .orElseGet(() -> necessitiesRepository.save(new DailyNecessities(name, category, unit, 0, null, center)));
    }

    // 저장
    public DailyNecessities save(DailyNecessities item) {
        return necessitiesRepository.save(item);
    }

}

    // 특정 센터의 생필품 전체 조회
    public List<DailyNecessitiesDto> getByCenter(Long centerId) {
        List<DailyNecessities> necessities = necessitiesRepository.findByProvidedBy_Id(centerId);
        return necessities.stream()
                .map(DailyNecessitiesDto::fromEntity)
                .collect(Collectors.toList());
    }
}