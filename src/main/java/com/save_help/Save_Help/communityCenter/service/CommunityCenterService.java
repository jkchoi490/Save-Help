package com.save_help.Save_Help.communityCenter.service;

import com.save_help.Save_Help.communityCenter.dto.CommunityCenterDto;
import com.save_help.Save_Help.communityCenter.entity.CenterType;
import com.save_help.Save_Help.communityCenter.entity.CommunityCenter;
import com.save_help.Save_Help.communityCenter.repository.CommunityCenterRepository;
import com.save_help.Save_Help.map.KakaoMapService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;


import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCenterService {

    private final CommunityCenterRepository repository;
    private final KakaoMapService kakaoMapService;


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




    public CommunityCenter registerCenter(CommunityCenter center) {
        // 1. 센터 이름으로 위도/경도 자동 조회
        double[] coordinates = kakaoMapService.getCoordinatesByName(center.getName());
        center.setLatitude(coordinates[0]);
        center.setLongitude(coordinates[1]);

        // 2️. 저장
        return repository.save(center);
    }

     // 거리 계산 (Haversine 공식)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    //특정 타입의 센터 중 주어진 좌표 기준 반경 내 센터 목록 조회
    public List<CommunityCenter> findCentersNearLocationByType(double userLat, double userLng, double radiusKm, CenterType type) {
        List<CommunityCenter> centers = repository.findByTypeAndActiveTrue(type);

        return centers.stream()
                .filter(c -> c.getLatitude() != null && c.getLongitude() != null)
                .filter(c -> calculateDistance(userLat, userLng, c.getLatitude(), c.getLongitude()) <= radiusKm)
                .sorted(Comparator.comparingDouble(c ->
                        calculateDistance(userLat, userLng, c.getLatitude(), c.getLongitude())))
                .toList();
    }

}
