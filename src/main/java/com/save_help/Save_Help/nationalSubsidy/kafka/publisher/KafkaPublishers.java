package com.save_help.Save_Help.nationalSubsidy.kafka.publisher;

import com.save_help.Save_Help.nationalSubsidy.kafka.*;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserEligibilityUpdatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.*;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaPublishers {

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
}
