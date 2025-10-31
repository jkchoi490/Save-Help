package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.emergency.entity.Emergency;
import com.save_help.Save_Help.emergency.entity.EmergencyStatus;
import com.save_help.Save_Help.emergency.repository.EmergencyRepository;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperActivityStatus;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class HelperAssignmentService {

    private final HelperRepository helperRepository;
    private final EmergencyRepository emergencyRepository;
    private final RedissonClient redissonClient;

    /**
     * 아직 헬퍼가 배정되지 않은 긴급 상황 자동 배치
     */
    @Transactional
    public void assignHelpersAutomatically() {
        // 헬퍼가 지정되지 않았고, 해결되지 않은(RESOLVED, CANCELLED 제외) 응급 요청 조회
        List<Emergency> activeEmergencies = emergencyRepository.findByAssignedHelperIsNullAndStatusIn(
                List.of(EmergencyStatus.PENDING, EmergencyStatus.REQUESTED, EmergencyStatus.ASSIGNED, EmergencyStatus.IN_PROGRESS)
        );

        // 추후 자동 배치 로직 구현 가능
    }

    // 거리 계산 (임시)
    private double distance(String loc1, String loc2) {
        return Math.random();
    }

    /**
     * 공통 배치 로직
     */
    private void assignHelperInternal(Helper helper, Emergency emergency) {
        if (!helper.isAvailable() || helper.getActivityStatus() != HelperActivityStatus.AVAILABLE) {
            throw new IllegalStateException("해당 헬퍼는 이미 배치되었거나 현재 이용할 수 없습니다.");
        }

        helper.setAvailable(false);
        helper.setActivityStatus(HelperActivityStatus.BUSY);
        emergency.setAssignedHelper(helper);
    }

    /**
     * 헬퍼 락 획득 유틸
     */
    private RLock getHelperLock(Long helperId) {
        return redissonClient.getLock("lock:helper:" + helperId);
    }

    /**
     * 관리자 직접 지정
     */
    @Transactional
    public void assignByAdmin(Long helperId, Long emergencyId) {
        RLock lock = getHelperLock(helperId);
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
            if (!locked)
                throw new IllegalStateException("다른 요청에서 해당 헬퍼를 배치 중입니다. 잠시 후 다시 시도해주세요.");

            Helper helper = helperRepository.findById(helperId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 ID의 헬퍼를 찾을 수 없습니다. (ID: " + helperId + ")"));

            Emergency emergency = emergencyRepository.findById(emergencyId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 ID의 긴급상황을 찾을 수 없습니다. (ID: " + emergencyId + ")"));

            assignHelperInternal(helper, emergency);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락을 획득하는 중 스레드가 인터럽트되었습니다.", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * 사용자 직접 배치
     */
    @Transactional
    public void assignByUser(Long helperId, Long emergencyId) {
        RLock lock = getHelperLock(helperId);
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
            if (!locked)
                throw new IllegalStateException("다른 사용자가 이미 해당 헬퍼를 지정 중입니다. 잠시 후 다시 시도해주세요.");

            Helper helper = helperRepository.findById(helperId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 ID의 헬퍼를 찾을 수 없습니다. (ID: " + helperId + ")"));

            Emergency emergency = emergencyRepository.findById(emergencyId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 ID의 긴급상황을 찾을 수 없습니다. (ID: " + emergencyId + ")"));

            assignHelperInternal(helper, emergency);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락을 획득하는 중 스레드가 인터럽트되었습니다.", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * 시스템 자동 배치
     */
    @Transactional
    public void assignAutomatically(Long emergencyId) {
        Helper candidate = helperRepository.findTopAvailableHelper()
                .orElseThrow(() -> new IllegalStateException("현재 사용 가능한 헬퍼가 없습니다."));

        RLock lock = getHelperLock(candidate.getId());
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
            if (!locked)
                throw new IllegalStateException("다른 프로세스에서 해당 헬퍼를 배치 중입니다.");

            Helper helper = helperRepository.findById(candidate.getId())
                    .orElseThrow(() -> new EntityNotFoundException("헬퍼 정보를 찾을 수 없습니다. (ID: " + candidate.getId() + ")"));

            Emergency emergency = emergencyRepository.findById(emergencyId)
                    .orElseThrow(() -> new EntityNotFoundException("긴급상황 정보를 찾을 수 없습니다. (ID: " + emergencyId + ")"));

            assignHelperInternal(helper, emergency);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락을 획득하는 중 스레드가 인터럽트되었습니다.", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) lock.unlock();
        }
    }

    /**
     * 긴급상황 종료 시 헬퍼 해제
     */
    @Transactional
    public void releaseHelper(Long helperId) {
        RLock lock = getHelperLock(helperId);
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
            if (!locked)
                throw new IllegalStateException("헬퍼 해제 요청을 처리할 수 없습니다. 잠시 후 다시 시도해주세요.");

            Helper helper = helperRepository.findById(helperId)
                    .orElseThrow(() -> new EntityNotFoundException("헬퍼 정보를 찾을 수 없습니다. (ID: " + helperId + ")"));

            helper.setAvailable(true);
            helper.setActivityStatus(HelperActivityStatus.AVAILABLE);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락을 획득하는 중 스레드가 인터럽트되었습니다.", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) lock.unlock();
        }
    }
}
