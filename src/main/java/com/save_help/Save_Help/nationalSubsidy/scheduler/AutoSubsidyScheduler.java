package com.save_help.Save_Help.nationalSubsidy.scheduler;

import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoSubsidyScheduler {

    private final NationalSubsidyService subsidyService;

    // 매일 7시에 실행 (한국시간)
    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Seoul")
    public void runAutoApplyJob() {
        log.info("자동 보조금 신청 작업 시작");
        subsidyService.autoApplyForAllUsers();
        log.info("✅ 자동 보조금 신청 작업 완료");
    }
}
