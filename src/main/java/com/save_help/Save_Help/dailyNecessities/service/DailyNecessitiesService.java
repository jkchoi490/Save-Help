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

    // 특정 센터의 생필품 전체 조회
    public List<DailyNecessitiesDto> getByCenter(Long centerId) {
        List<DailyNecessities> necessities = necessitiesRepository.findByProvidedBy_Id(centerId);
        return necessities.stream()
                .map(DailyNecessitiesDto::fromEntity)
                .collect(Collectors.toList());
    }
}