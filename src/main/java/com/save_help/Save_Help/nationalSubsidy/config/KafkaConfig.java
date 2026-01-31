package com.save_help.Save_Help.nationalSubsidy.config;

import com.save_help.Save_Help.nationalSubsidy.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.*;
import org.springframework.context.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.*;
import org.springframework.util.backoff.FixedBackOff;

import java.util.*;

@EnableKafka
@Configuration
public class KafkaConfig {


    @Bean public NewTopic nationalSubsidyScheduledTopic() { return new NewTopic(KafkaTopics.NATIONALSUBSIDY_SCHEDULED, 1, (short) 1); }


    @Bean
    public ProducerFactory<String, Object> producerFactory(org.springframework.core.env.Environment env) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        // 권장 옵션
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(org.springframework.core.env.Environment env) {
        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*"); // 내부 DTO만 신뢰하도록 좁혀도 됨

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, jsonDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> cf,
            KafkaTemplate<String, Object> kafkaTemplate
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cf);
        factory.setConcurrency(3);

        // 수동 커밋
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                template,
                (record, ex) -> {
                    String topic = record.topic();
                    if (KafkaTopics.SUBSIDY_CREATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.SUBSIDY_CREATED_DLQ, record.partition());
                    }
                    if (KafkaTopics.USER_CREATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.USER_CREATED_DLQ, record.partition());
                    }
                    if (KafkaTopics.USER_ELIGIBILITY_UPDATED.equals(topic)) {
                        return new TopicPartition(KafkaTopics.USER_ELIGIBILITY_UPDATED_DLQ, record.partition());
                    }
                    return new TopicPartition(topic + ".dlq", record.partition());
                }
        );

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 3L));
        return handler;
    }

    // (개발편의) 토픽 자동 생성
    @Bean public NewTopic t1() { return new NewTopic(KafkaTopics.SUBSIDY_CREATED, 3, (short) 1); }
    @Bean public NewTopic t2() { return new NewTopic(KafkaTopics.USER_CREATED, 3, (short) 1); }
    @Bean public NewTopic t3() { return new NewTopic(KafkaTopics.USER_ELIGIBILITY_UPDATED, 3, (short) 1); }

    @Bean public NewTopic d1() { return new NewTopic(KafkaTopics.SUBSIDY_CREATED_DLQ, 3, (short) 1); }
    @Bean public NewTopic d2() { return new NewTopic(KafkaTopics.USER_CREATED_DLQ, 3, (short) 1); }
    @Bean public NewTopic d3() { return new NewTopic(KafkaTopics.USER_ELIGIBILITY_UPDATED_DLQ, 3, (short) 1); }

}
