package com.save_help.Save_Help.dailyNecessities.kafka.producer;

import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.kafka.event.DailyNecessitiesCreated;
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
}