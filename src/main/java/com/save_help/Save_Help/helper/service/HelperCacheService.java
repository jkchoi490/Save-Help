package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperCacheDto;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelperCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final HelperRepository helperRepository;

    private static final String HELPER_CACHE_KEY = "helper:available";

    /**
     * 헬퍼 전체 리스트를 Redis에 캐싱 (TTL: 7분)
     */
    public void cacheAvailableHelpers() {
        List<Helper> helpers = helperRepository.findAll()
                .stream()
                .filter(Helper::isAvailable)
                .collect(Collectors.toList());

        List<HelperCacheDto> dtoList = helpers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        redisTemplate.opsForValue().set(HELPER_CACHE_KEY, dtoList, Duration.ofMinutes(7));
    }

    /**
     * 캐시에서 조회 (없으면 DB → 캐시 리빌드)
     */
    @SuppressWarnings("unchecked")
    public List<HelperCacheDto> getAvailableHelpersCached() {
        Object cached = redisTemplate.opsForValue().get(HELPER_CACHE_KEY);
        if (cached != null) {
            return (List<HelperCacheDto>) cached;
        }
        cacheAvailableHelpers();
        return (List<HelperCacheDto>) redisTemplate.opsForValue().get(HELPER_CACHE_KEY);
    }

    /**
     * 특정 헬퍼 상태/위치 변경 시 캐시 갱신
     */
    public void updateHelperInCache(Helper helper) {
        List<HelperCacheDto> current = getAvailableHelpersCached();
        List<HelperCacheDto> updated = current.stream()
                .filter(h -> !h.getId().equals(helper.getId()))
                .collect(Collectors.toList());

        if (helper.isAvailable()) {
            updated.add(toDto(helper));
        }

        redisTemplate.opsForValue().set(HELPER_CACHE_KEY, updated, Duration.ofMinutes(5));
    }

    /**
     * 헬퍼 캐시 DTO 변환
     */
    private HelperCacheDto toDto(Helper helper) {
        return HelperCacheDto.builder()
                .id(helper.getId())
                .name(helper.getName())
                .role(helper.getRole())
                .available(helper.isAvailable())
                .activityStatus(helper.getActivityStatus())
                .latitude(helper.getLatitude())
                .longitude(helper.getLongitude())
                .trustScore(helper.getTrustScore())
                .lastLocationUpdateTime(helper.getLastLocationUpdateTime())
                .build();
    }
}
