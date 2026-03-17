package com.save_help.Save_Help.dailyNecessities.kafka.consumer;

import com.save_help.Save_Help.dailyNecessities.kafka.event.UserNecessityEvent;
import com.save_help.Save_Help.dailyNecessities.service.DailyNecessitiesService;
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
    public void consume(UserNecessityEvent event) {
        //log.info("생필품 자동 신청 이벤트 수신 userId={}, trigger={}", event.userId(), event.triggerType());
        //autoApplyService.autoApplyForUser(event.userId(), event.triggerType());
    }
}
