package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.*;
import com.save_help.Save_Help.dailyNecessities.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyNecessitiesAutoReorderService {

    private final DailyNecessitiesAutoReorderSettingRepository reorderRepo;
    private final UserNecessityRequestService requestService;
    private final DailyNecessitiesStockService stockService;
    private final DailyNecessitiesNotificationService notificationService;

    //7시간마다 자동 재신청 수행
    @Scheduled(cron = "0 0 */7 * * *")
    public void processAutoReorders() {
        List<AutoReorderSetting> dueReorders =
                reorderRepo.findByActiveTrueAndNextReorderAtBefore(LocalDateTime.now());

        for (AutoReorderSetting setting : dueReorders) {
            try {
                DailyNecessities item = stockService.getItem(setting.getItemId());

                // 재고 확인
                if (item.getStock() < setting.getQuantity()) {
                    notificationService.notifyAdmin(
                            "재고 부족: " + item.getName() + " 자동 재신청 실패 (userId=" + setting.getUserId() + ")"
                    );
                    continue;
                }

                // 자동 신청 생성
                requestService.createRequest(
                        setting.getUserId(),
                        setting.getItemId(),
                        setting.getQuantity()
                );

                // 다음 신청일 업데이트
                setting.setNextReorderAt(LocalDateTime.now().plusDays(setting.getIntervalDays()));
                reorderRepo.save(setting);

                // 사용자 알림
                notificationService.notifyUser(setting.getUserId(),
                        item.getName() + "이(가) 자동으로 신청되었습니다.");

                log.info("자동 재신청 완료: user={} item={} qty={}", setting.getUserId(), setting.getItemId(), setting.getQuantity());

            } catch (Exception e) {
                log.error("자동 재신청 실패 (settingId={}): {}", setting.getId(), e.getMessage());
            }
        }
    }

    public AutoReorderSetting createSetting(Long userId, Long itemId, int quantity, int intervalDays) {
        AutoReorderSetting setting = AutoReorderSetting.builder()
                .userId(userId)
                .itemId(itemId)
                .quantity(quantity)
                .intervalDays(intervalDays)
                .nextReorderAt(LocalDateTime.now().plusDays(intervalDays))
                .active(true)
                .build();
        return reorderRepo.save(setting);
    }

    public void deactivate(Long settingId) {
        reorderRepo.findById(settingId).ifPresent(setting -> {
            setting.setActive(false);
            reorderRepo.save(setting);
        });
    }

    public List<AutoReorderSetting> getUserSettings(Long userId) {
        return reorderRepo.findAll().stream()
                .filter(s -> s.getUserId().equals(userId))
                .toList();
    }
}
