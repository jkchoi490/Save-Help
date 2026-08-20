package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.NationalSubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.NationalSubsidySavedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NationalSubsidyCreatedProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void publish(
            NationalSubsidySavedEvent savedEvent
    ) {

        NationalSubsidyCreatedEvent kafkaEvent =
                NationalSubsidyCreatedEvent.builder()
                        .eventId(savedEvent.eventId())
                        .subsidyId(savedEvent.subsidyId())
                        .occurredAt(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                KafkaTopics.NATIONAL_SUBSIDY_CREATED,
                savedEvent.subsidyId().toString(),
                kafkaEvent
        );

        log.info(
                "신규 국가보조금 Kafka 이벤트 발행: eventId={}, subsidyId={}",
                kafkaEvent.getEventId(),
                kafkaEvent.getSubsidyId()
        );
    }
}