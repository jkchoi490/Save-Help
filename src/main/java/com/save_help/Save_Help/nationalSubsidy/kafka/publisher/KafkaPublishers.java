package com.save_help.Save_Help.nationalSubsidy.kafka.publisher;

import com.save_help.Save_Help.nationalSubsidy.kafka.*;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.*;

import java.util.UUID;

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
        kafkaTemplate.send(KafkaTopics.SUBSIDY_CREATED, String.valueOf(e.subsidyId()), event);
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
}
