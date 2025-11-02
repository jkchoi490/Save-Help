package com.save_help.Save_Help.helper.scheduler;

import com.save_help.Save_Help.helper.service.HelperCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class HelperCacheScheduler {

    private final HelperCacheService cacheService;

    /**
     * 일정시간마다 캐시 리빌드 (DB → Redis)
     */
    @Scheduled(fixedRate = 370_000)
    public void refreshHelperCache() {
        cacheService.cacheAvailableHelpers();
    }
}
