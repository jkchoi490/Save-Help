package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperRecommendationDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperRole;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelperRecommendationService {

    private final HelperRepository helperRepository;

    // 추천 로직
    public List<HelperRecommendationDto> recommendHelpers(Double latitude, Double longitude, HelperRole role) {
        List<Helper> candidates = helperRepository.findByRoleAndAvailableTrue(role);

        return candidates.stream()
                .map(helper -> {
                    double distance = calculateDistance(latitude, longitude,
                            helper.getLatitude(), helper.getLongitude());
                    return HelperRecommendationDto.builder()
                            .id(helper.getId())
                            .name(helper.getName())
                            .role(helper.getRole().name())
                            .latitude(helper.getLatitude())
                            .longitude(helper.getLongitude())
                            .distanceKm(distance)
                            .build();
                })
                .sorted(Comparator.comparingDouble(HelperRecommendationDto::getDistanceKm))
                .limit(5) // 상위 5명만 추천
                .collect(Collectors.toList());
    }

    // Haversine formula (지구 거리 계산)
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구 반지름 (km)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
