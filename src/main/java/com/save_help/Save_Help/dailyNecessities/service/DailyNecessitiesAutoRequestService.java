package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.dailyNecessities.repository.UserNecessityRequestRepository;
import com.save_help.Save_Help.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DailyNecessitiesAutoRequestService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final UserNecessityRequestRepository requestRepository;
    private final RedissonClient redissonClient;

    public DailyNecessitiesAutoRequestService(
            DailyNecessitiesRepository necessitiesRepository,
            UserNecessityRequestRepository requestRepository,
            RedissonClient redissonClient
    ) {
        this.necessitiesRepository = necessitiesRepository;
        this.requestRepository = requestRepository;
        this.redissonClient = redissonClient;
    }

    // Redisson 락 사용
    private RLock getLock(Long necessityId) {
        return redissonClient.getLock("lock:necessity:" + necessityId);
    }


    // 사용자가 과거 수급받았던 생필품이 재고에 들어온 경우 자동 신청
    @Transactional
    public void autoRequestBasedOnPastReceipts(User user, List<DailyNecessities> pastReceivedItems) {
        for (DailyNecessities pastItem : pastReceivedItems) {
            List<DailyNecessities> availableList =
                    necessitiesRepository.findAvailableByNameOrCategory(pastItem.getName(), pastItem.getCategory());

            for (DailyNecessities available : availableList) {
                RLock lock = getLock(available.getId());
                boolean locked = false;

                try {
                    locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
                    if (!locked) continue;

                    if (available.getStock() > 0) {
                        createAutoRequest(user, available, 1);
                        available.setStock(available.getStock() - 1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("락을 획득하는 중 오류가 발생했습니다.", e);
                } finally {
                    if (locked && lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            }
        }
    }


    // 사용자가 요청한 생필품이 재고에 존재할 경우 자동 신청 처리
    @Transactional
    public void autoRequestIfStockAvailable(User user, String requestedName, NecessityCategory category, int quantity) {
        List<DailyNecessities> availableList =
                necessitiesRepository.findAvailableByNameOrCategory(requestedName, category);

        if (availableList.isEmpty())
            throw new EntityNotFoundException("요청한 생필품이 현재 재고에 존재하지 않습니다.");

        DailyNecessities item = availableList.get(0);
        RLock lock = getLock(item.getId());
        boolean locked = false;

        try {
            locked = lock.tryLock(3, 7, TimeUnit.SECONDS);
            if (!locked)
                throw new IllegalStateException("다른 사용자가 동일한 품목을 처리 중입니다. 잠시 후 다시 시도해주세요.");

            if (item.getStock() >= quantity) {
                createAutoRequest(user, item, quantity);
                item.setStock(item.getStock() - quantity);
            } else {
                throw new IllegalStateException("요청한 수량만큼의 재고가 부족합니다.");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("락을 획득하는 중 스레드가 인터럽트되었습니다.", e);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }


    // 자동 신청 생성 메서드
    private void createAutoRequest(User user, DailyNecessities item, int quantity) {
        UserNecessityRequest request = new UserNecessityRequest();
        request.setUser(user);
        request.setItem(item);
        request.setQuantity(quantity);
        request.setStatus(UserNecessityRequest.RequestStatus.AUTO_APPLIED);
        requestRepository.save(request);
    }
}
