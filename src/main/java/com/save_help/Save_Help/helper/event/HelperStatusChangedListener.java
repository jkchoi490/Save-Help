package com.save_help.Save_Help.helper.event;


import com.save_help.Save_Help.helper.service.HelperNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelperStatusChangedListener {

    private final HelperNotificationService notificationService;

    @Async // 비동기로 처리
    @EventListener
    public void onHelperStatusChanged(HelperStatusChangedEvent event) {
        log.info("헬퍼 상태 변경 이벤트 감지: {} → {}", event.getOldStatus(), event.getNewStatus());
        String message = String.format("헬퍼 %d의 상태가 [%s] → [%s]로 변경되었습니다.",
                event.getHelperId(),
                event.getOldStatus(),
                event.getNewStatus());

        // 관리자/센터에 알림 전송 (문자, 푸시, 이메일 등)
        notificationService.notifyAdmins(message);
    }
}