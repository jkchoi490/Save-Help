package com.save_help.Save_Help.dailyNecessities.service;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesAlertLog;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesAlertLogRepository;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.messaging.service.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesAlertService {

    private final DailyNecessitiesRepository necessitiesRepository;
    private final TwilioService twilioService;
    private final DailyNecessitiesAlertLogRepository alertLogRepository;

    private static final int LOW_STOCK_THRESHOLD = 7;

    public void sendLowStockAlerts() {
        List<DailyNecessities> lowStockItems = necessitiesRepository.findByStockLessThan(LOW_STOCK_THRESHOLD);

        if (lowStockItems.isEmpty()) {
            return;
        }

        StringBuilder messageBuilder = new StringBuilder("[긴급 재고 알림]\n");
        for (DailyNecessities item : lowStockItems) {
            messageBuilder.append("- ")
                    .append(item.getName())
                    .append(" (잔여: ")
                    .append(item.getStock())
                    .append(")\n");
        }

        String message = messageBuilder.toString();


        List<String> adminPhones = List.of("", "");

        for (String phone : adminPhones) {
            twilioService.sendSms(phone, message);
        }

        // 로그 저장
        DailyNecessitiesAlertLog log = new DailyNecessitiesAlertLog();
        log.setMessage(message);
        log.setType(DailyNecessitiesAlertLog.AlertType.LOW_STOCK);
        log.setCreatedAt(LocalDateTime.now());
        alertLogRepository.save(log);
    }
}