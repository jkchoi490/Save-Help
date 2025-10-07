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

    //생성
    public DailyNecessities create(DailyNecessitiesDto dto) {
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

        return necessitiesRepository.save(item);
    }

    //전체 조회
    public List<DailyNecessities> getAll() {
        return necessitiesRepository.findByActiveTrue();
    }

    //카테고리별 조회
    public List<DailyNecessities> getByCategory(NecessityCategory category) {
        return necessitiesRepository.findByCategory(category);
    }

    //이름 검색
    public List<DailyNecessities> searchByName(String name) {
        return necessitiesRepository.findByNameContainingIgnoreCase(name);
    }

    //수정
    public DailyNecessities update(Long id, DailyNecessitiesDto dto) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        item.setName(dto.getName());
        item.setCategory(dto.getCategory());
        item.setUnit(dto.getUnit());
        item.setStock(dto.getStock());
        item.setExpirationDate(dto.getExpirationDate());

        return necessitiesRepository.save(item);
    }

    //삭제(비활성화)
    public void delete(Long id) {
        DailyNecessities item = necessitiesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        item.deactivate();
        necessitiesRepository.save(item);
    }
}
