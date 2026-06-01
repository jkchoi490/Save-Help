package com.save_help.Save_Help.dailyNecessities.kafka.producer;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitiesCreated;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessityEligibilityEvent;
import com.save_help.Save_Help.nationalSubsidy.kafka.topic.KafkaTopics;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCreated(DailyNecessities dailyNecessities) {
        DailyNecessitiesCreated event = DailyNecessitiesCreated.builder()
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

    public void publishDailyNecessitiesCreated(DailyNecessitiesCreated event) {

    }
}