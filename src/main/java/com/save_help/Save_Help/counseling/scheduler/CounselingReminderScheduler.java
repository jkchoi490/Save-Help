package com.save_help.Save_Help.counseling.scheduler;

import com.save_help.Save_Help.counseling.entity.Counseling;
import com.save_help.Save_Help.counseling.entity.CounselingStatus;
import com.save_help.Save_Help.counseling.entity.NotificationType;
import com.save_help.Save_Help.counseling.entity.RecipientType;
import com.save_help.Save_Help.counseling.repository.CounselingRepository;

import com.save_help.Save_Help.counseling.service.CounselingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CounselingReminderScheduler {

    private final CounselingRepository counselingRepository;
    private final CounselingService counselingService;

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void remind() {
        sendReminderForMinutes(49); //상담 시작 49분 전 알림 1회
        sendReminderForMinutes(7);  //상담 시작 7분 전 알림 1회
    }

    private void sendReminderForMinutes(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.plusMinutes(minutes).minusSeconds(30);
        LocalDateTime to = now.plusMinutes(minutes).plusSeconds(30);

        List<Counseling> targets =
                counselingRepository.findByStatusAndStartAtBetween(CounselingStatus.ACCEPTED, from, to);

        for (Counseling c : targets) {
            createReminder(c, minutes);
        }
    }

    private void createReminder(Counseling c, int minutes) {
        if (c.getUser() == null || c.getStartAt() == null) return;

        String deeplink = "/counselings/" + c.getId();

        // 사용자 알림
        counselingService.createIfAbsent(
                RecipientType.USER,
                c.getUser().getId(),
                NotificationType.COUNSELING_REMINDER,
                "상담 리마인더",
                "상담이 " + minutes + "분 후 시작돼요. 시작 시간: " + c.getStartAt(),
                deeplink,
                "counseling:" + c.getId() + ":reminder:" + minutes + ":user"
        );

        // 상담사(헬퍼) 알림 - counselor가 있을 때만
        if (c.getCounselor() != null) {
            counselingService.createIfAbsent(
                    RecipientType.HELPER,
                    c.getCounselor().getId(),
                    NotificationType.COUNSELING_REMINDER,
                    "상담 리마인더",
                    "상담이 " + minutes + "분 후 시작돼요. 상담 ID: " + c.getId(),
                    deeplink,
                    "counseling:" + c.getId() + ":reminder:" + minutes + ":helper"
            );
        }
    }
}
