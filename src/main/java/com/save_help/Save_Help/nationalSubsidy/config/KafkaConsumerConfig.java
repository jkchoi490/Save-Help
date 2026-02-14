package com.save_help.Save_Help.nationalSubsidy.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(template,
                        (record, ex) -> new TopicPartition(record.topic() + ".DLQ", record.partition()));

        FixedBackOff backOff = new FixedBackOff(1000L, 7L);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backOff);

        errorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("[Kafka Retry] topic={}, partition={}, offset={}, attempt={}, error={}",
                    record.topic(), record.partition(), record.offset(), deliveryAttempt, ex.toString());
        });

        return new DefaultErrorHandler(recoverer, backOff);
    }
}
