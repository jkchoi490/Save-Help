package com.save_help.Save_Help.communityCenter.service;

import com.save_help.Save_Help.communityCenter.dto.CommunityCenterDto;
import com.save_help.Save_Help.communityCenter.entity.CenterType;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCenterService {

    private final CommunityCenterRepository repository;

    public List<CommunityCenter> getAllCenters() {
        return repository.findAll();
    }

    public Optional<CommunityCenter> getCenterById(Long id) {
        return repository.findById(id);
    }

    public CommunityCenter createCenter(CommunityCenterDto dto) {
        CommunityCenter center = new CommunityCenter();
        center.setName(dto.getName());
        center.setType(dto.getType());
        center.setLatitude(dto.getLatitude());
        center.setLongitude(dto.getLongitude());
        center.setPhoneNumber(dto.getPhoneNumber());
        center.setActive(dto.isActive());
        return repository.save(center);
    }

    public CommunityCenter updateCenter(Long id, CommunityCenterDto dto) {
        CommunityCenter center = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("센터를 찾을 수 없습니다."));

        center.setName(dto.getName());
        center.setType(dto.getType());
        center.setLatitude(dto.getLatitude());
        center.setLongitude(dto.getLongitude());
        center.setPhoneNumber(dto.getPhoneNumber());
        center.setActive(dto.isActive());
        return repository.save(center);
    }

    public void deleteCenter(Long id) {
        repository.deleteById(id);
    }

    public List<CommunityCenter> getCentersByType(CenterType type) {
        return repository.findByType(type);
    }

    public List<CommunityCenter> getActiveCenters() {
        return repository.findByActiveTrue();
    }

    public List<CommunityCenter> getActiveCentersByType(CenterType type) {
        return repository.findByTypeAndActiveTrue(type);
    }
}
