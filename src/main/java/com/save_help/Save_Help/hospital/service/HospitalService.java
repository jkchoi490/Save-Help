package com.save_help.Save_Help.hospital.service;

import com.save_help.Save_Help.hospital.dto.HospitalRequestDto;
import com.save_help.Save_Help.hospital.dto.HospitalResponseDto;
import com.save_help.Save_Help.hospital.entity.Hospital;
import com.save_help.Save_Help.hospital.entity.HospitalType;
import com.save_help.Save_Help.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    //Pub/Sub 발행 추가
    private final StringRedisTemplate redisTemplate;
    private final ChannelTopic hospitalTopic;
    private static final String BED_KEY_PREFIX = "hospital:";

    public HospitalResponseDto createHospital(HospitalRequestDto dto) {
        Hospital hospital = new Hospital();
        hospital.setName(dto.getName());
        hospital.setType(dto.getType());
        hospital.setPhoneNumber(dto.getPhoneNumber());
        hospital.setLatitude(dto.getLatitude());
        hospital.setLongitude(dto.getLongitude());
        hospital.setActive(dto.isActive());
        hospital.setBedCount(dto.getBedCount());

        hospitalRepository.save(hospital);
        return toDto(hospital);
    }

    public HospitalResponseDto getHospital(Long id) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));
        return toDto(hospital);
    }

    public List<HospitalResponseDto> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public HospitalResponseDto updateHospital(Long id, HospitalRequestDto dto) {
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        hospital.setName(dto.getName());
        hospital.setType(dto.getType());
        hospital.setPhoneNumber(dto.getPhoneNumber());
        hospital.setLatitude(dto.getLatitude());
        hospital.setLongitude(dto.getLongitude());
        hospital.setActive(dto.isActive());
        hospital.setBedCount(dto.getBedCount());

        return toDto(hospital);
    }

    public void deleteHospital(Long id) {
        hospitalRepository.deleteById(id);
    }

    //남은 병상 수 조회
    public int getRemainingBeds(Long id) {
        String key = buildKey(id);
        String bedCount = redisTemplate.opsForValue().get(key);

        if (bedCount != null) {
            return Integer.parseInt(bedCount);
        }

        // Redis에 없으면 DB에서 읽고 Redis에 초기화
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));
        int count = hospital.getBedCount();
        redisTemplate.opsForValue().set(key, String.valueOf(count));
        return count;
    }
    //병상 감소
    /*
    public void decreaseBedCount(Long hospitalId) {
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        hospital.decreaseBedCount();
        hospitalRepository.save(hospital);
    }*/

    /**
     * 병상 부족 시 가장 가까운 병원 탐색
     * @param latitude - 요청 위치 위도
     * @param longitude - 요청 위치 경도
     * @return 가장 가까운 이용 가능한 병원 or null
     */
    public Hospital findNearestAvailableHospital(double latitude, double longitude) {
        List<Hospital> hospitals = hospitalRepository.findByActiveTrue();

        return hospitals.stream()
                .filter(h -> h.getBedCount() > 0)
                .min(Comparator.comparingDouble(h -> calculateDistance(latitude, longitude, h.getLatitude(), h.getLongitude())))
                .orElse(null);
    }


    //위도/경도로 거리 계산 (Haversine formula)

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        if (lat1 == 0 || lon1 == 0 || lat2 == 0 || lon2 == 0) return Double.MAX_VALUE;

        final int R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // distance in km
    }

    private HospitalResponseDto toDto(Hospital hospital) {
        return HospitalResponseDto.builder()
                .id(hospital.getId())
                .name(hospital.getName())
                .type(hospital.getType())
                .phoneNumber(hospital.getPhoneNumber())
                .latitude(hospital.getLatitude())
                .longitude(hospital.getLongitude())
                .active(hospital.isActive())
                .bedCount(hospital.getBedCount())
                .remainingBeds(getRemainingBeds(hospital.getId()))
                .build();
    }

    // 사용자 위치 기준, 타입별 가장 가까운 병원 조회
    public Hospital findNearestHospital(double userLat, double userLng, HospitalType type) {
        return hospitalRepository.findByTypeAndActiveTrue(type)
                .stream()
                .filter(h -> h.getLatitude() != null && h.getLongitude() != null)
                .min(Comparator.comparingDouble(h ->
                        calculateDistance(userLat, userLng, h.getLatitude(), h.getLongitude())))
                .orElse(null);
    }

    //남은 병상이 있고 사용자 위치 기준 가까운 병원 조회
    public Hospital findNearestHospitalWithAvailableBed(double userLat, double userLng, HospitalType type) {
        return hospitalRepository.findByTypeAndActiveTrue(type)
                .stream()
                .filter(h -> h.getLatitude() != null && h.getLongitude() != null)
                .filter(h -> h.getBedCount() > 0)
                .min(Comparator.comparingDouble(h ->
                        calculateDistance(userLat, userLng, h.getLatitude(), h.getLongitude())))
                .orElse(null);
    }

    public void setBedCount(Long hospitalId, int bedCount) {
        redisTemplate.opsForValue().set(buildKey(hospitalId), String.valueOf(bedCount));
        publishBedUpdate(hospitalId, bedCount);
    }

    public void decreaseBedCount(Long hospitalId) {
        Long updated = redisTemplate.opsForValue().decrement(buildKey(hospitalId));

        if (updated != null && updated >= 0) {
            publishBedUpdate(hospitalId, updated.intValue());

            // 병상이 줄어들었으니 DB도 동기화 (optional)
            hospitalRepository.findById(hospitalId)
                    .ifPresent(h -> h.setBedCount(updated.intValue()));
        } else {
            redisTemplate.opsForValue().set(buildKey(hospitalId), "0");
            throw new IllegalStateException("병상이 더 이상 남아있지 않습니다.");
        }
    }

    public void increaseBedCount(Long hospitalId) {
        Long updated = redisTemplate.opsForValue().increment(buildKey(hospitalId));
        publishBedUpdate(hospitalId, updated != null ? updated.intValue() : 0);
    }

    private void publishBedUpdate(Long hospitalId, int bedCount) {
        String message = String.format("{\"hospitalId\":%d,\"bedCount\":%d}", hospitalId, bedCount);
        redisTemplate.convertAndSend(hospitalTopic.getTopic(), message);
    }

    private String buildKey(Long hospitalId) {
        return BED_KEY_PREFIX + hospitalId + ":beds";
    }





}
