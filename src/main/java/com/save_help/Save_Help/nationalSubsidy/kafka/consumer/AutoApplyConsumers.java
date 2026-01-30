package com.save_help.Save_Help.nationalSubsidy.kafka.consumer;

import com.save_help.Save_Help.nationalSubsidy.kafka.*;
import com.save_help.Save_Help.nationalSubsidy.service.NationalSubsidyAutoApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoApplyConsumers {

    private final NationalSubsidyAutoApplyService autoApplyService;

    @KafkaListener(topics = KafkaTopics.SUBSIDY_CREATED, groupId = "subsidy-auto-apply")
    public void onSubsidyCreated(SubsidyCreatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForSubsidyToUsers(event.subsidyId());
        ack.acknowledge();
    }

    @KafkaListener(topics = KafkaTopics.USER_CREATED, groupId = "user-auto-apply")
    public void onUserCreated(UserCreatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForUserToSubsidies(event.userId());
        ack.acknowledge();
    }

    @KafkaListener(topics = KafkaTopics.USER_ELIGIBILITY_UPDATED, groupId = "user-auto-apply")
    public void onUserEligibilityUpdated(UserEligibilityUpdatedEvent event, Acknowledgment ack) {
        autoApplyService.applyForUserToSubsidies(event.userId());
        ack.acknowledge();
    }
}