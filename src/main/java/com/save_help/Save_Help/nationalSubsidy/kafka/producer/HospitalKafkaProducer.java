package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.HospitalBedCountEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishBedUpdated(
            Long hospitalId,
            Integer remainingBeds
    ) {

        HospitalBedCountEvent event =
                HospitalBedCountEvent.builder()
                        .hospitalId(hospitalId)
                        .remainingBeds(remainingBeds)
                        .occurredAt(System.currentTimeMillis())
                        .build();

        kafkaTemplate.send(
                KafkaTopics.HOSPITAL_BEDCOUNT_UPDATED,
                String.valueOf(hospitalId),
                event
        );
    }
}