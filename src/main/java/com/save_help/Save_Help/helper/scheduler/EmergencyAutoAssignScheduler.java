package com.save_help.Save_Help.helper.scheduler;

import com.save_help.Save_Help.helper.service.HelperAssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmergencyAutoAssignScheduler {

    private final HelperAssignmentService helperAssignmentService;

    // 7분마다 미배정된 응급상황 자동 확인
    @Scheduled(fixedRate = 420000) // 7분
    public void autoAssign() {
        helperAssignmentService.assignHelpersAutomatically();
    }
}