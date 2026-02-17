package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserEligibilityUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoApplyProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishSubsidyOpened(SubsidyCreatedEvent event) {
        kafkaTemplate.send("subsidy.created", String.valueOf(event.subsidyId()), event);
    }

    public void publishUserEligibilityUpdated(UserEligibilityUpdatedEvent event) {
        kafkaTemplate.send("user.eligibility.updated", String.valueOf(event.userId()), event);
    }
}
