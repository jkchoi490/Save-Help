package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserNationalSubsidyEligibilityEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class NationalSubsidyEligibilityProducer {

    private final KafkaTemplate<String, UserNationalSubsidyEligibilityEvent> kafkaTemplate;

    public void publish(
            Long userId,
            String triggerType
    ) {

        UserNationalSubsidyEligibilityEvent event =
                UserNationalSubsidyEligibilityEvent.builder()
                        .eventId(UUID.randomUUID().toString())
                        .userId(userId)
                        .triggerType(triggerType)
                        .occurredAt(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                KafkaTopics.USER_SUBSIDY_ELIGIBILITY_CHECK,
                userId.toString(),
                event
        );

        log.info(
                "국가보조금 자동 신청 이벤트 발행: eventId={}, userId={}, triggerType={}",
                event.getEventId(),
                event.getUserId(),
                event.getTriggerType()
        );
    }
}