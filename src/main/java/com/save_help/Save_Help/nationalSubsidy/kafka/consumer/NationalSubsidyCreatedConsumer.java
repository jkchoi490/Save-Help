package com.save_help.Save_Help.nationalSubsidy.kafka.consumer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.NationalSubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NationalSubsidyCreatedConsumer {

    private final NationalSubsidyService nationalSubsidyAutoApplyService;

    @KafkaListener(
            topics = KafkaTopics.NATIONAL_SUBSIDY_CREATED,
            groupId = "national-subsidy-created-auto-apply-group"
    )
    public void consume(
            NationalSubsidyCreatedEvent event
    ) {

        log.info(
                "신규 국가보조금 이벤트 수신: eventId={}, subsidyId={}",
                event.getEventId(),
                event.getSubsidyId()
        );

        int applicationCount =
                nationalSubsidyAutoApplyService
                        .autoApplyNewSubsidy(event);

        log.info(
                "신규 국가보조금 자동 신청 완료: subsidyId={}, applicationCount={}",
                event.getSubsidyId(),
                applicationCount
        );
    }
}
