package com.save_help.Save_Help.dailyNecessities.kafka.producer;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitiesCreatedEvent;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessityEligibilityEvent;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitySavedEvent;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesRepository;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserDailyNecessitiesEligibilityEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyNecessitiesPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Spring 내부 이벤트 발행
    private final ApplicationEventPublisher applicationEventPublisher;

    private final DailyNecessitiesRepository dailyNecessitiesRepository;

    public void publishCreated(DailyNecessities dailyNecessities) {
        DailyNecessitiesCreatedEvent event = DailyNecessitiesCreatedEvent.builder()
                .necessityId(dailyNecessities.getId())
                .title(dailyNecessities.getName())
                .category(dailyNecessities.getCategory().name())
                .build();


        kafkaTemplate.send("DAILY_NECESSITY_CREATED", String.valueOf(dailyNecessities.getId()), event);
    }

    public void publishEligibilityChanged(DailyNecessityEligibilityEvent event) {
        kafkaTemplate.send(
                KafkaTopics.DAILY_NECESSITY_ELIGIBILITY_CHANGED,
                String.valueOf(event.getNecessityId()),
                event
        );
    }


    // 생필품 생성 이벤트 발행
    public void publishDailyNecessitiesCreated(
            DailyNecessitiesCreatedEvent event
    ) {

        kafkaTemplate.send(
                "DAILY_NECESSITY_CREATED",
                String.valueOf(event.getNecessityId()),
                event
        );
    }


    //사용자 데이터에 따른 자동신청 기능 개발을 위한 Producer
    public void send(UserDailyNecessitiesEligibilityEvent event) {
        kafkaTemplate.send(
                KafkaTopics.DAILY_NECESSITIES_USER_ELIGIBILITY_CHECK,
                event.userId().toString(),
                event
        );
    }

    // 생필품 DB 저장 완료 이벤트 발행
    public void publishEvent(
            DailyNecessitySavedEvent event
    ) {

        applicationEventPublisher.publishEvent(event);
    }

    // DB Transaction Commit 이후 Kafka 이벤트 발행
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void publish(
            DailyNecessitySavedEvent savedEvent
    ) {

        DailyNecessities dailyNecessities =
                dailyNecessitiesRepository
                        .findById(savedEvent.necessityId())
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "생필품을 찾을 수 없습니다. necessityId="
                                                + savedEvent.necessityId()
                                )
                        );

        DailyNecessitiesCreatedEvent kafkaEvent =
                DailyNecessitiesCreatedEvent.builder()
                        .eventId(System.currentTimeMillis())
                        .necessityId(dailyNecessities.getId())
                        .title(dailyNecessities.getName())
                        .category(
                                dailyNecessities.getCategory() != null
                                        ? dailyNecessities.getCategory().name()
                                        : null
                        )
                        .stock(dailyNecessities.getStock())
                        .occurredAt(LocalDateTime.now())
                        .build();

        kafkaTemplate.send(
                KafkaTopics.DAILY_NECESSITIES_CREATED,
                String.valueOf(dailyNecessities.getId()),
                kafkaEvent
        );

        log.info(
                "생필품 생성 Kafka 이벤트 발행: eventId={}, necessityId={}, title={}, category={}, stock={}",
                kafkaEvent.getEventId(),
                kafkaEvent.getNecessityId(),
                kafkaEvent.getTitle(),
                kafkaEvent.getCategory(),
                kafkaEvent.getStock()
        );
    }
}