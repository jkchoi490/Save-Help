package com.save_help.Save_Help.nationalSubsidy.kafka.producer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.HospitalBedCountEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
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

    private void validateBedUpdated(Long hospitalId, Integer remainingBeds) {
        if (hospitalId == null) {
            throw new IllegalArgumentException("hospitalId는 필수입니다.");
        }

        if (remainingBeds == null) {
            throw new IllegalArgumentException("remainingBeds는 필수입니다.");
        }

    }
}