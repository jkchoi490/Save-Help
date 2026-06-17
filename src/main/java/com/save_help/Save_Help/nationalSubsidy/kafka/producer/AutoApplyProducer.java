package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.SubsidyCreatedEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.event.UserEligibilityUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutoApplyProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    private void send(
            String topic,
            String key,
            Object payload
    ) {

        kafkaTemplate.send(topic, key, payload)
                .whenComplete((result, ex) -> {

                    if (ex != null) {
                        log.error(
                                "[Kafka Publish FAIL] topic={}, key={}",
                                topic,
                                key,
                                ex
                        );
                        return;
                    }

                    log.info(
                            "[Kafka Publish OK] topic={}, key={}, offset={}",
                            topic,
                            key,
                            result.getRecordMetadata().offset()
                    );
                });
    }

    public void publishSubsidyOpened(SubsidyCreatedEvent event) {
        kafkaTemplate.send("subsidy.created", String.valueOf(event.subsidyId()), event);
    }

    public void publishUserEligibilityUpdated(UserEligibilityUpdatedEvent event) {
        kafkaTemplate.send("user.eligibility.updated", String.valueOf(event.userId()), event);
    }


}
