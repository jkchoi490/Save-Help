package com.save_help.Save_Help.nationalSubsidy.kafka.publisher;

import com.save_help.Save_Help.nationalSubsidy.kafka.*;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedInternalEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserEligibilityUpdatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.*;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublishers {

    private static final String EVENT_VERSION = "v1";
    private static final String PRODUCER = "save-help-national-subsidy";


    private final KafkaTemplate<String, Object> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onSubsidyCreated(SubsidyCreatedInternalEvent e) {
        SubsidyCreatedEvent event = new SubsidyCreatedEvent(
                e.subsidyId(),
                UUID.randomUUID().toString(),
                System.currentTimeMillis()
        );

        kafkaTemplate.send(KafkaTopics.SUBSIDY_CREATED, String.valueOf(e.subsidyId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("[Kafka Publish FAIL] topic={}, key={}, event={}",
                                KafkaTopics.SUBSIDY_CREATED, e.subsidyId(), event, ex);
                        return;
                    }
                    var md = result.getRecordMetadata();
                    log.info("[Kafka Publish OK] topic={}, key={}, partition={}, offset={}, ts={}",
                            KafkaTopics.SUBSIDY_CREATED, e.subsidyId(), md.partition(), md.offset(), md.timestamp());
                });
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserCreated(UserCreatedInternalEvent e) {
        UserCreatedEvent event = new UserCreatedEvent(
                e.userId(),
                UUID.randomUUID().toString(),
                System.currentTimeMillis()
        );
        kafkaTemplate.send(KafkaTopics.USER_CREATED, String.valueOf(e.userId()), event);

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEligibilityChanged(UserEligibilityChangedInternalEvent e) {
        UserEligibilityUpdatedEvent event = new UserEligibilityUpdatedEvent(
                e.userId(),
                e.changedFields(),
                UUID.randomUUID().toString(),
                System.currentTimeMillis()
        );
        kafkaTemplate.send(KafkaTopics.USER_ELIGIBILITY_UPDATED, String.valueOf(e.userId()), event);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(SavedNationalSubsidyUser e) {
        RequirementsNationalSubsidy event = new RequirementsNationalSubsidy(
                UUID.randomUUID().toString(),
                e.userId(),
                System.currentTimeMillis(),
                e.reason()
        );

        kafkaTemplate.send(
                KafkaTopics.USER_ELIGIBILITY_APPLIED,
                String.valueOf(e.userId()),
                event
        );
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onUserSaved(SavedNationalSubsidyUser e) {
        RequirementsNationalSubsidy event = new RequirementsNationalSubsidy(
                UUID.randomUUID().toString(),
                e.userId(),
                System.currentTimeMillis(),
                e.reason()
        );

        // key=userId로 보내면 같은 유저는 같은 파티션
        kafkaTemplate.send(KafkaTopics.USER_ELIGIBILITY_APPLIED, String.valueOf(e.userId()), event);

    }

    private void send(String topic, String key, Object payload, String eventId) {

        long occurredAt = System.currentTimeMillis();

        String traceId = mdc("traceId");

        Message<Object> msg = MessageBuilder.withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.KEY, key)

                .setHeader("eventId", eventId)
                .setHeader("eventType", payload.getClass().getSimpleName())
                .setHeader("eventVersion", EVENT_VERSION)
                .setHeader("occurredAt", occurredAt)
                .setHeader("producer", PRODUCER)
                .setHeader("traceId", traceId)

                .build();

        kafkaTemplate.send(msg).whenComplete((result, ex) -> {

            if (ex != null) {
                log.error("[Kafka Publish FAIL] topic={}, key={}, eventId={}, payload={}",
                        topic, key, eventId, payload, ex);
                return;
            }

            var md = result.getRecordMetadata();

            log.info("[Kafka Publish OK] topic={}, key={}, eventId={}, partition={}, offset={}, ts={}",
                    topic, key, eventId,
                    md.partition(), md.offset(), md.timestamp());
        });
    }

    private static String mdc(String key) {
        try {
            return MDC.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public void publishSubsidyApplicationCreated(
            Long applicationId,
            Long userId,
            Long subsidyId
    ) {
        SubsidyApplicationCreatedEvent event =
                SubsidyApplicationCreatedEvent.builder()
                        .applicationId(applicationId)
                        .userId(userId)
                        .subsidyId(subsidyId)
                        .eventId(UUID.randomUUID().toString())
                        .occurredAt(System.currentTimeMillis())
                        .build();

        send(
                KafkaTopics.SUBSIDY_APPLICATION_CREATED,
                String.valueOf(applicationId),
                event
        );
    }
}
