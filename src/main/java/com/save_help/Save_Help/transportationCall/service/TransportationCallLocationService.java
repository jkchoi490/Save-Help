package com.save_help.Save_Help.transportationCall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.save_help.Save_Help.transportationCall.dto.DriverLocationMessage;
import com.save_help.Save_Help.transportationCall.dto.DriverLocationUpdateRequest;
import com.save_help.Save_Help.transportationCall.entity.TransportationCall;
import com.save_help.Save_Help.transportationCall.entity.TransportationCallStatus;
import com.save_help.Save_Help.transportationCall.repository.TransportationCallRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TransportationCallLocationService {

    private static final Duration TTL = Duration.ofMinutes(5);

    private final TransportationCallRepository callRepository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     운전자 위치 업데이트(실시간):
      - 상태 검증(배치/출발 이후 등)
      - driver 존재 검증
      - Redis에 마지막 위치 저장(TTL)
      - WS로 브로드캐스트할 메시지 반환
     */
    @Transactional(readOnly = true)
    public DriverLocationMessage updateAndCache(Long callId, DriverLocationUpdateRequest req) {
        TransportationCall call = callRepository.findById(callId)
                .orElseThrow(() -> new EntityNotFoundException("TransportationCall not found: " + callId));

        // Helper로 지정된 운전자만 가능
        if (call.getDriver() == null) {
            throw new IllegalStateException("운전자가 배정되지 않았습니다.");
        }

        // 상태 검증: DISPATCHED ~ ARRIVED 전까지 위치 업데이트 허용
        TransportationCallStatus st = call.getStatus();
        if (st == TransportationCallStatus.REQUESTED) {
            throw new IllegalStateException("배치 전에는 위치를 업데이트할 수 없습니다.");
        }
        if (st == TransportationCallStatus.ARRIVED || st == TransportationCallStatus.CANCELLED) {
            throw new IllegalStateException("종료된 호출은 위치를 업데이트할 수 없습니다.");
        }

        long now = System.currentTimeMillis();
        DriverLocationMessage msg = new DriverLocationMessage(
                callId,
                call.getDriver().getId(),
                req.latitude(),
                req.longitude(),
                now
        );

        try {
            String json = objectMapper.writeValueAsString(msg);
            redisTemplate.opsForValue().set(lastKey(callId), json, TTL); // 마지막 위치 저장
        } catch (Exception e) {
            throw new IllegalStateException("위치 캐시 저장 실패", e);
        }

        return msg;
    }

    // 재접속/새로고침 시 마지막 위치 조회
    @Transactional(readOnly = true)
    public DriverLocationMessage getLastLocation(Long callId) {
        String json = redisTemplate.opsForValue().get(lastKey(callId));
        if (json == null) return null;

        try {
            return objectMapper.readValue(json, DriverLocationMessage.class);
        } catch (Exception e) {
            throw new IllegalStateException("위치 캐시 조회 실패", e);
        }
    }

    // 도착/종료 시 캐시 삭제
    @Transactional
    public void clearLastLocation(Long callId) {
        redisTemplate.delete(lastKey(callId));
    }

    private String lastKey(Long callId) {
        return "transportation:call:" + callId + ":driver:last_location";
    }
}