package com.save_help.Save_Help.nationalSubsidy.kafka.consumer;

import com.save_help.Save_Help.nationalSubsidy.kafka.event.HospitalBedCountEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HospitalBedCountConsumer {

    @KafkaListener(
            topics = KafkaTopics.HOSPITAL_BEDCOUNT_UPDATED,
            groupId = "hospital-bed-group"
    )
    public void consume(HospitalBedCountEvent event) {

        log.info(
                "병상 변경 hospitalId={}, beds={}",
                event.getHospitalId(),
                event.getRemainingBeds()
        );

        if (event.getRemainingBeds() <= 6) {

            log.warn(
                    "병상 부족 hospitalId={}",
                    event.getHospitalId()
            );
        }
    }

    private boolean isInvalidEvent(HospitalBedCountEvent event) {
        return event == null
                || event.getHospitalId() == null
                || event.getRemainingBeds() == null
                || event.getRemainingBeds() < 0;
    }
}