package com.save_help.Save_Help.dailyNecessities.kafka.consumer;

import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitiesCreatedEvent;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserDailyNecessitiesEligibilityEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyNecessityAutoApplyConsumer {

    private final DailyNecessitiesService dailyNecessitiesService;

    @KafkaListener(
            topics = KafkaTopics.NECESSITY_AUTO_APPLY_REQUEST,
            groupId = "necessity-auto-apply"
    )
    public void consume(DailyNecessitiesCreatedEvent event) {

        log.info("자동신청 이벤트 수신 EventId={}, NecessityId={}, title={}",
                event.getEventId(),
                event.getNecessityId(),
                event.getTitle());

        //dailyNecessitiesService.autoApplyForUser(
        //        event.userId(),
        //        event.triggerType()
       // );
    }

    // 생필품 자동 신청 consumer
    @KafkaListener(
            topics = KafkaTopics.DAILY_NECESSITIES_USER_ELIGIBILITY_CHECK,
            groupId = "daily-necessity-auto-apply-group"
    )
    public void consume(
            UserDailyNecessitiesEligibilityEvent event
    ) {
        log.info(
                "생필품 자동 신청 자격 이벤트 수신: eventId={}, userId={}, triggerType={}",
                event.eventId(),
                event.userId(),
                event.triggerType()
        );

        int applicationCount =
                dailyNecessitiesService.autoApply(event);

        log.info(
                "생필품 자동 신청 처리 완료: userId={}, applicationCount={}",
                event.userId(),
                applicationCount
        );
    }
}
